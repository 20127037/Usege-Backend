package com.group_1.trash.service;

import com.group_1.amqp.dto.ImagesInAlbumRequest;
import com.group_1.trash.producers.RabbitMQMessageProducer;
import com.group_1.sharedDynamoDB.exception.NoSuchElementFoundException;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.model.UserFileInAlbum;
import com.group_1.sharedDynamoDB.model.UserInfo;
import com.group_1.sharedDynamoDB.repository.*;
import com.group_1.trash.config.RabbitMQConfig;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * com.group_1.master.service
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 2:14 PM
 * Description: ...
 */
@Service
@AllArgsConstructor
public class TrashBinServiceImpl implements TrashBinService {

    private final UserFileRepository userFileRepository;
    private final UserRepository userRepository;
    private final UserDeletedFileRepository userDeletedFileRepository;
    private final UserFilesInAlbumRepository userFilesInAlbumRepository;
    private final RabbitMQMessageProducer messageProducer;
    private final RabbitMQConfig rabbitMQConfig;
    private static final int DEFAULT_CLEAR_DAYS = 7;

    @Override
    public List<UserFile> createDeletedFiles(String userId, @NonNull String... fileNames) {
        UserInfo userInfo = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        if (userInfo == null)
            throw new NoSuchElementFoundException(userId, "users");
        boolean hasAnyAlbum = userInfo.getAlbumCount() > 0;
        Map<String, Set<String>> mapAlbumsToImages = new HashMap<>();
        List<UserFile> resultSet = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        for (String fileName : fileNames)
        {
            UserFile deleted = userFileRepository.deleteRecordByKey(DynamoDbRepository.getKey(userId, fileName));
            if (deleted == null)
                continue;
            deleted.setRemainingDays(DEFAULT_CLEAR_DAYS);
            deleted.setUpdated(now.toString());
            //Delete image from albums
            if (hasAnyAlbum)
            {
                QueryResponse<UserFileInAlbum> userFileInAlbumQueryResponse = userFilesInAlbumRepository.query(
                        DynamoDbRepository.getQueryConditional(DynamoDbRepository.getKey(userId, fileName)),
                        null,
                        UserFileInAlbum.Indexes.FILE_NAME_INDEX,
                        userInfo.getAlbumCount().intValue(),
                        null,
                        false);
                List<UserFileInAlbum> fileInAlbums = userFileInAlbumQueryResponse.getResponse();
                if (fileInAlbums != null && !fileInAlbums.isEmpty())
                {
                    for (UserFileInAlbum album : fileInAlbums)
                    {
                        Set<String> images = mapAlbumsToImages.get(album.getAlbumName());
                        if (images == null)
                            images = new HashSet<>();
                        mapAlbumsToImages.putIfAbsent(album.getAlbumName(), images);
                        images.add(fileName);
                    }
                    deleted.setPreviousAlbums(fileInAlbums.stream().map(UserFileInAlbum::getAlbumName).collect(Collectors.toSet()));
                }
            }
            resultSet.add(userDeletedFileRepository.saveRecord(deleted));
            now = now.plusNanos(1L);
        }
        if (hasAnyAlbum && !mapAlbumsToImages.isEmpty())
        {
            for (Map.Entry<String, Set<String>> albumAndImages : mapAlbumsToImages.entrySet())
                messageProducer.publish(new ImagesInAlbumRequest(userId, albumAndImages.getKey(), albumAndImages.getValue().toArray(String[]::new)),
                        rabbitMQConfig.getInternalExchange(), rabbitMQConfig.getInternalAlbumDelKey());
        }
        userRepository.updateRecord(DynamoDbRepository.getKey(userId), i -> {
            i.setImgCount(i.getImgCount() - resultSet.size());
            i.setDeletedImgCount(i.getDeletedImgCount() + resultSet.size());
        });
        return resultSet;
    }

    @Override
    public List<UserFile> createDeletedFilesFromAllFiles(String userId) {
        UserInfo userInfo = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        QueryResponse<UserFile> userFileQueryResponse = userFileRepository.query(
                DynamoDbRepository.getQueryConditional(DynamoDbRepository.getKey(userId)),
                null,
                userInfo.getImgCount().intValue(),
                null,
                false,
                UserFile.Fields.fileName);
        List<UserFile> userFiles = userFileQueryResponse.getResponse();
        if (userFiles == null || userFiles.isEmpty())
            throw new NoSuchElementFoundException(userId, "files");
        return createDeletedFiles(userId, userFiles.stream().map(UserFile::getFileName).toArray(String[]::new));
    }

    @Override
    public List<UserFile> clearDeletedFiles(String userId, String... fileNames) {
        List<UserFile> resultSet = new ArrayList<>();
        for (String fileName : fileNames)
        {
            UserFile deleted = userDeletedFileRepository.deleteRecordByKey(DynamoDbRepository.getKey(userId, fileName));
            //Remove file from s3
            resultSet.add(deleted);
        }

        userRepository.updateRecord(DynamoDbRepository.getKey(userId), i
                -> i.setDeletedImgCount(i.getDeletedImgCount() - resultSet.size()));
        return resultSet;
    }

    @Override
    public List<UserFile> clearAll(String userId) {
        UserInfo userInfo = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        if (userInfo == null || userInfo.getDeletedImgCount() == 0)
            throw new NoSuchElementFoundException(userId, "users");
        QueryResponse<UserFile> userFileQueryResponse = userDeletedFileRepository.query(
                DynamoDbRepository.getQueryConditional(DynamoDbRepository.getKey(userId)),
                null,
                userInfo.getDeletedImgCount().intValue(),
                null,
                false,
                UserFile.Fields.fileName);
        List<UserFile> userFiles = userFileQueryResponse.getResponse();
        if (userFiles == null || userFiles.isEmpty())
            throw new NoSuchElementFoundException(userId, "files");
        return clearDeletedFiles(userId, userFiles.stream().map(UserFile::getFileName).toArray(String[]::new));
    }

    @Override
    public List<UserFile> restoreFiles(String userId, String... fileNames) {
        List<UserFile> resultSet = new ArrayList<>();
        Map<String, Set<String>> mapAlbumsToImages = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        for (String fileName : fileNames)
        {
            UserFile deleted = userDeletedFileRepository.deleteRecordByKey(DynamoDbRepository.getKey(userId, fileName));
            if (deleted == null)
                continue;
            Set<String> previousAlbums = deleted.getPreviousAlbums();
            deleted.setRemainingDays(null);
            deleted.setUpdated(now.toString());
            deleted.setPreviousAlbums(null);
            resultSet.add(userFileRepository.saveRecord(deleted));
            if (previousAlbums != null)
            {
                for (String album : previousAlbums)
                {
                    Set<String> images = mapAlbumsToImages.get(album);
                    if (images == null)
                        images = new HashSet<>();
                    mapAlbumsToImages.putIfAbsent(album, images);
                    images.add(fileName);
                }
            }
            now = now.plusNanos(1L);
        }
        for (Map.Entry<String, Set<String>> albumAndImages : mapAlbumsToImages.entrySet())
            messageProducer.publish(new ImagesInAlbumRequest(userId, albumAndImages.getKey(), albumAndImages.getValue().toArray(String[]::new)),
                    rabbitMQConfig.getInternalExchange(), rabbitMQConfig.getInternalAlbumAddKey());
        userRepository.updateRecord(DynamoDbRepository.getKey(userId), i -> {
            i.setImgCount(i.getImgCount() + resultSet.size());
            i.setDeletedImgCount(i.getDeletedImgCount() - resultSet.size());
        });
        return resultSet;
    }

    @Override
    public List<UserFile> restoreAll(String userId) {
        UserInfo userInfo = userRepository.getRecordByKey(DynamoDbRepository.getKey(userId));
        if (userInfo == null || userInfo.getDeletedImgCount() == 0)
            throw new NoSuchElementFoundException(userId, "users");
        QueryResponse<UserFile> userDelFileResponse = userDeletedFileRepository.query(
                DynamoDbRepository.getQueryConditional(DynamoDbRepository.getKey(userId)),
                null,
                userInfo.getDeletedImgCount().intValue(),
                null,
                false
        );
        List<UserFile> userDelFiles = userDelFileResponse.getResponse();
        if (userDelFiles == null || userDelFiles.isEmpty())
            throw new NoSuchElementFoundException(userId, "trash bin");
        return restoreFiles(userId, userDelFiles.stream().map(UserFile::getFileName).toArray(String[]::new));
    }
}
