package com.group_1.uploadFile.service;

import com.group_1.uploadFile.dto.UserFileDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * com.group_1.uploadFile.service
 * Created by NhatLinh - 19127652
 * Date 4/19/2023 - 12:38 PM
 * Description: ...
 */
public interface FileService {
    void userUploadFile(String userId, UserFileDto userFileDto, MultipartFile file);
    void userDeleteFile(String userId, String fileId);
    void testUploadFile(MultipartFile file);
}
