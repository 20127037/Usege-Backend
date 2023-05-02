package com.group_1.uploadFile.service;

import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.model.UserInfo;
import com.group_1.sharedDynamoDB.repository.DynamoDbRepository;
import com.group_1.sharedDynamoDB.repository.UserFileRepository;
import com.group_1.sharedDynamoDB.repository.UserRepository;
import com.group_1.sharedS3.repository.FileRepository;
import com.group_1.uploadFile.dto.UserFileRefUploadDto;
import com.group_1.uploadFile.dto.UserFileUploadDto;
import com.group_1.uploadFile.exception.ExceedSpaceException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * com.group_1.uploadFile.service
 * Created by NhatLinh - 19127652
 * Date 4/19/2023 - 12:39 PM
 * Description: ...
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    private final UserFileRepository userFileDbRepository;
    @Value("${amazon.aws.s3-bucket}")
    private String bucket;

    public FileServiceImpl(FileRepository fileRepository, UserRepository userRepository, UserFileRepository userFileDbRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.userFileDbRepository = userFileDbRepository;
    }

    @SneakyThrows
    @Override
    public UserFile userUploadFile(String userId, UserFileUploadDto userFileDto, MultipartFile file) {
        log.info(String.format("UserId ----> %s", userId));
        log.info(String.format("----> %s", userFileDto));
        byte[] fileBytes = file.getBytes();
        String fileName = UUID.randomUUID().toString();
        String contentType = file.getContentType();
        long fileSize = file.getSize() / 1024;
        String fileUri = String.format("http://localhost:4566/%s/%s/%s", bucket, userId, fileName);

        log.info(String.format("FILE-NAME ----> %s", fileName));
        log.info(String.format("CONTENT-TYPE ----> %s", contentType));
        log.info(String.format("FILE-SIZE ----> %s", fileSize));

        UserInfo userInfo = userRepository.getRecordById(userId);
        //Check file size
        if (userInfo.getUsedSpace() + fileSize >= userInfo.getMaxSpace())
            throw new ExceedSpaceException(userInfo.getMaxSpace(), userInfo.getUsedSpace(), fileSize);

        //Create userFile record
        UserFile userFile = UserFile
                .builder()
                .userId(userId)
                .fileName(fileName)
                .contentType(contentType)
                .sizeInKb(fileSize)
                .updated(LocalDateTime.now().toString())
                .tinyUri(fileUri)
                .normalUri(fileUri)
                .tags(userFileDto.getTags())
                .date(userFileDto.getDate())
                .description(userFileDto.getDescription())
                .location(userFileDto.getLocation())
                //.isDeleted(false)
                .isFavourite(false)
                .build();
        userFileDbRepository.saveRecord(userFile);
        log.info(String.format("USER FILE ----> %s", userFile.toString()));
        //Update user info
        userRepository.updateRecord(userId, u -> {
            // Increase used space
            u.setUsedSpace(u.getUsedSpace() + fileSize);
            // Increase imgCount
            u.setImgCount(u.getImgCount() + 1);
        });
        fileRepository.uploadFile(userId, fileName, contentType, fileBytes);
        return userFile;
    }

    @Override
    public UserFile userUploadRefFile(String userId, UserFileRefUploadDto refUploadDto) {
        //Create userFile record
        UserFile file = userFileDbRepository.getRecordByKey(DynamoDbRepository.getKey(userId, refUploadDto.fileName()));
        if (file != null)
            return file;
        String now = LocalDateTime.now().toString();
        UserFile userFile = UserFile
                .builder()
                .userId(userId)
                .fileName(refUploadDto.fileName())
                .contentType(ContentType.IMAGE_JPEG.getMimeType())
                .sizeInKb(0L)
                .updated(now)
                .tinyUri(refUploadDto.tinyUri())
                .normalUri(refUploadDto.uri())
                .date(now)
                .description(refUploadDto.description())
                //.isDeleted(false)
                .isFavourite(false)
                .build();
        userFileDbRepository.saveRecord(userFile);
        //Update user info
        userRepository.updateRecord(userId, u -> {
            // Increase imgCount
            u.setImgCount(u.getImgCount() + 1);
        });
        return userFile;
    }

    @Override
    public void testUploadFile(MultipartFile file) {
//        fileRepository.createFolder("9fb8b578-3fd0-4794-bff0-557659afbcfc");
        try {
            fileRepository.uploadFile("9fb8b578-3fd0-4794-bff0-557659afbcfc", file.getName(), file.getContentType(), file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
