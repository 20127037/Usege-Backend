package com.group_1.master.service;

import com.group_1.master.dto.QueryFilesInAlbumResponse;
import com.group_1.sharedDynamoDB.exception.NoSuchElementFoundException;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.model.UserFileInAlbum;
import com.group_1.sharedDynamoDB.model.UserInfo;
import com.group_1.sharedDynamoDB.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

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
    private final AlbumService albumService;
    private static final int DEFAULT_CLEAR_DAYS = 7;

    @Override
    public List<UserFile> createDeletedFiles(String userId, String... fileNames) {
        UserInfo userInfo = userRepository.getRecordById(userId);
        if (userInfo == null)
            throw new NoSuchElementFoundException(userId, "users");

        Map<String, Set<String>> mapAlbumsToImages = new HashMap<>();
        List<UserFile> resultSet = new ArrayList<>();
        for (String fileName : fileNames)
        {
            UserFile deleted = userFileRepository.deleteRecordByKey(DynamoDbRepository.getKey(userId, fileName));
            if (deleted == null)
                continue;
            deleted.setRemainingDays(DEFAULT_CLEAR_DAYS);

            //Delete image from albums
            QueryResponse<UserFileInAlbum> userFileInAlbumQueryResponse = userFilesInAlbumRepository.query(
                    DynamoDbRepository.getQueryConditional(userId, fileName),
                    null,
                    UserFileInAlbum.Indexes.FILE_NAME_INDEX,
                    userInfo.getAlbumCount().intValue(),
                    null,
                    false,
                    UserFileInAlbum.Fields.albumName
            );
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

            resultSet.add(userDeletedFileRepository.saveRecord(deleted));
        }
        for (Map.Entry<String, Set<String>> albumAndImages : mapAlbumsToImages.entrySet())
            albumService.deleteImagesFromAlbum(userId, albumAndImages.getKey(), albumAndImages.getValue().toArray(String[]::new));
        userRepository.updateRecord(userId, i -> {
            i.setImgCount(i.getImgCount() - resultSet.size());
            i.setImgCount(i.getDeletedImgCount() + resultSet.size());
        });
        return resultSet;
    }

    @Override
    public List<UserFile> clearDeletedFiles(String userId, String... fileNames) {
        List<UserFile> resultSet = new ArrayList<>();
        for (String fileName : fileNames)
            resultSet.add(userDeletedFileRepository.deleteRecordByKey(DynamoDbRepository.getKey(userId, fileName)));
        userRepository.updateRecord(userId, i -> i.setDeletedImgCount(i.getDeletedImgCount() - resultSet.size()));
        return resultSet;
    }

    @Override
    public List<UserFile> clearAll(String userId) {
        return null;
    }

    @Override
    public List<UserFile> restoreFiles(String userId, String... fileNames) {
        List<UserFile> resultSet = new ArrayList<>();
        Map<String, Set<String>> mapAlbumsToImages = new HashMap<>();
        for (String fileName : fileNames)
        {
            UserFile deleted = userDeletedFileRepository.getRecordByKey(DynamoDbRepository.getKey(userId, fileName));
            if (deleted == null)
                continue;
            Set<String> previousAlbums = deleted.getPreviousAlbums();
            deleted.setRemainingDays(null);
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
        }
        for (Map.Entry<String, Set<String>> albumAndImages : mapAlbumsToImages.entrySet())
            albumService.addImagesToAlbum(userId, albumAndImages.getKey(), albumAndImages.getValue().toArray(String[]::new));
        userRepository.updateRecord(userId, i -> {
            i.setImgCount(i.getImgCount() + resultSet.size());
            i.setImgCount(i.getDeletedImgCount() - resultSet.size());
        });
        return resultSet;
    }

    @Override
    public List<UserFile> restoreAll(String userId) {
        UserInfo userInfo = userRepository.getRecordById(userId);
        if (userInfo == null)
            throw new NoSuchElementFoundException(userId, "users");
        QueryResponse<UserFile> userDelFileResponse = userDeletedFileRepository.query(
                DynamoDbRepository.getQueryConditional(userId, null),
                null,
                userInfo.getDeletedImgCount().intValue(),
                null,
                false,
                UserFile.Fields.fileName
        );
        List<UserFile> userDelFiles = userDelFileResponse.getResponse();
        if (userDelFiles == null || userDelFiles.isEmpty())
            throw new NoSuchElementFoundException(userId, "trash bin");
        return restoreFiles(userId, userDelFiles.stream().map(UserFile::getFileName).toArray(String[]::new));
    }

    @Override
    public QueryResponse<UserFile> queryImages(String userId, int limit, Map<String, AttributeValue> startKey) {
        QueryResponse<UserFile> userFileQueryResponse = userDeletedFileRepository
                .query(DynamoDbRepository.getQueryConditional(userId, null),
                        null,
                        limit,
                        startKey,
                        false,
                        UserFileInAlbum.Fields.fileName);
        List<UserFile> resultSet = new ArrayList<>();
        for (UserFile userFileInAlbum : userFileQueryResponse.getResponse())
        {
            UserFile userFile = userFileRepository.getRecordByKey(DynamoDbRepository.getKey(userId, userFileInAlbum.getFileName()));
            if (userFile == null)
                continue;
            resultSet.add(userFile);
        }
        return QueryResponse.<UserFile>builder()
                .prevEvaluatedKey(null)
                .nextEvaluatedKey(userFileQueryResponse.getNextEvaluatedKey())
                .response(resultSet)
                .build();
    }
}
