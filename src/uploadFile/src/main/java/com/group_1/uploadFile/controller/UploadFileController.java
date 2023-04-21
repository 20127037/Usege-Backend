package com.group_1.uploadFile.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group_1.uploadFile.dto.UserFileDto;
import com.group_1.uploadFile.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("{id}/upload")
    public ResponseEntity<String> uploadFiles(@PathVariable String id,
                                              @RequestParam("userFileDto") String userFileDtoJson,
                                              @RequestParam("file") MultipartFile file) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        UserFileDto userFileDto = objectMapper.readValue(userFileDtoJson, UserFileDto.class);
        fileService.userUploadFile(id, userFileDto, file);

        return ResponseEntity.ok("Upload File Successfully");
    }

    @PostMapping
    public void testUploadFile(@RequestParam("file") MultipartFile file)
    {
        fileService.testUploadFile(file);
    }
}
