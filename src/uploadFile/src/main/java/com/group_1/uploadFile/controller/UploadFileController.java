package com.group_1.uploadFile.controller;

import com.group_1.uploadFile.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * com.group_1.uploadFile.controller
 * Created by NhatLinh - 19127652
 * Date 4/19/2023 - 12:33 PM
 * Description: ...
 */
@RestController
@AllArgsConstructor
public class UploadFileController {

    private final FileService fileService;

    @PostMapping
    public void testUploadFile(@RequestParam("file") MultipartFile file)
    {
        fileService.testUploadFile(file);
    }
}
