package com.group_1.uploadFile.service;

import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.model.UserInfo;
import com.group_1.sharedDynamoDB.repository.UserFileDbRepository;
import com.group_1.sharedDynamoDB.repository.UserRepository;
import com.group_1.sharedS3.repository.FileRepository;
import com.group_1.sharedS3.repository.S3Repository;
import lombok.AllArgsConstructor;
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
    private final FileRepository fileRepository;
    private final UserFileDbRepository userFileDbRepository;
    private final UserRepository userRepository;

    @Override
    public void userUploadFile(String userId, MultipartFile file) {

        String contentType = file.getContentType();
        Long fileSize = file.getSize();

        UserFile userFile = UserFile
                .builder()
                .userId(userId)
                .fileId(UUID.randomUUID().toString())
                .contentType(contentType)
                .sizeInKb(fileSize / 1024)
                .updated(LocalDateTime.now().toString())
                .fileUrl("")
                .build();

        userFileDbRepository.saveRecord(userFile);

        // Tăng giá trị fileCount của imgCount
        UserInfo userInfo = userRepository.getRecordById(userId);
        userInfo.setImgCount(userInfo.getImgCount() + 1);
        userRepository.saveRecord(userInfo);
        
    }

    @Override
    public void userDeleteFile(String userId, String fileId) {

    }

    @Override
    public void testUploadFile(MultipartFile file) {

    }
}
