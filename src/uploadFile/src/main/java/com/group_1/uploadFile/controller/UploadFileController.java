package com.group_1.uploadFile.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group_1.uploadFile.dto.UserFileDto;
import com.group_1.uploadFile.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

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

    @PostMapping(value = "{id}", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<String> uploadFiles(@PathVariable String id,
                                              @RequestPart("info") UserFileDto info,
                                              @RequestPart("file") MultipartFile file)
    {
        String url = fileService.userUploadFile(id, info, file);
        if (url == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.created(URI.create(url)).body(url);
    }

    @PostMapping
    public void testUploadFile(@RequestParam("file") MultipartFile file)
    {
        fileService.testUploadFile(file);
    }
}
