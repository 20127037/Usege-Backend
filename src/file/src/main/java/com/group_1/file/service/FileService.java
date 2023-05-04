package com.group_1.file.service;

import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.file.dto.UserFileRefUploadDto;
import com.group_1.file.dto.UserFileUploadDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * com.group_1.uploadFile.service
 * Created by NhatLinh - 19127652
 * Date 4/19/2023 - 12:38 PM
 * Description: ...
 */
public interface FileService {
    UserFile userUploadFile(String userId, UserFileUploadDto userFileDto, MultipartFile file);
    UserFile userUploadRefFile(String userId, UserFileRefUploadDto refUploadDto);
    UserFile updateFile(String userId, UserFile update);
}
