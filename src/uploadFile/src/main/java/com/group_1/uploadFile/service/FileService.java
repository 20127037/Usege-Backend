package com.group_1.uploadFile.service;

import com.group_1.uploadFile.dto.UserFileRefUploadDto;
import com.group_1.uploadFile.dto.UserFileUploadDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * com.group_1.uploadFile.service
 * Created by NhatLinh - 19127652
 * Date 4/19/2023 - 12:38 PM
 * Description: ...
 */
public interface FileService {
    String userUploadFile(String userId, UserFileUploadDto userFileDto, MultipartFile file);
    String userUploadRefFile(String userId, UserFileRefUploadDto refUploadDto);
    void userDeleteFile(String userId, String fileId);
    void testUploadFile(MultipartFile file);
}
