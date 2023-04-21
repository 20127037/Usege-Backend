package com.group_1.uploadFile.service;

import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.model.UserInfo;
import com.group_1.sharedDynamoDB.repository.UserFileDbRepository;
import com.group_1.sharedDynamoDB.repository.UserRepository;
import com.group_1.sharedS3.repository.FileRepository;
import com.group_1.sharedS3.repository.S3Repository;
import com.group_1.uploadFile.dto.UserFileDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    private final UserFileDbRepository userFileDbRepository;

    @Override
    public void userUploadFile(String userId, UserFileDto userFileDto, MultipartFile file) {
        //fileRepository.createFolder("9fb8b578-3fd0-4794-bff0-557659afbcfc");
        LOGGER.info(String.format("UserId ----> %s", userId));
        LOGGER.info(String.format("----> %s", userFileDto));
        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String fileName = file.getName();
        String contentType = file.getContentType();
        Long fileSize = file.getSize();
        String fileUrl = String.format("http://localhost:4566/%s/%s", userId, fileName);

        UserFile userFile = UserFile
                .builder()
                .userId(userId)
                .fileId(UUID.randomUUID().toString())
                .contentType(contentType)
                .sizeInKb(fileSize / 1024)
                .updated(LocalDateTime.now().toString())
                .fileUrl(fileUrl)
                .tags(userFileDto.getTags())
                .date(userFileDto.getDate())
                .description(userFileDto.getDescription())
                .location(userFileDto.getLocation())
                .uriLocal(userFileDto.getUri())
                .build();

        userFileDbRepository.saveRecord(userFile);

        // Tăng giá trị fileCount của imgCount
        UserInfo userInfo = userRepository.getRecordById("userId");
        LOGGER.info(String.format("UserInfo ----> %s", userInfo));
//        userInfo.setImgCount(userInfo.getImgCount() + 1);
//        userRepository.saveRecord(userInfo);

        fileRepository.uploadFile("9fb8b578-3fd0-4794-bff0-557659afbcfc", fileName, contentType, fileBytes);
    }

    @Override
    public void userDeleteFile(String userId, String fileId) {

    }

    @Override
    public void testUploadFile(MultipartFile file) {
        fileRepository.createFolder("9fb8b578-3fd0-4794-bff0-557659afbcfc");
        try {
            fileRepository.uploadFile("9fb8b578-3fd0-4794-bff0-557659afbcfc", file.getName(), file.getContentType(), file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
