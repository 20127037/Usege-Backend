package com.group_1.uploadFile.service;

import com.group_1.sharedS3.repository.FileRepository;
import com.group_1.sharedS3.repository.S3Repository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    @Override
    public void userUploadFile(String userId, MultipartFile file) {

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
