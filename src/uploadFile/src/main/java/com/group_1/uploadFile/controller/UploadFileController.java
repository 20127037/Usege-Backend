package com.group_1.uploadFile.controller;

import com.group_1.uploadFile.dto.UserFileRefUploadDto;
import com.group_1.uploadFile.dto.UserFileUploadDto;
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
                                              @RequestPart("info") UserFileUploadDto info,
                                              @RequestPart("file") MultipartFile file)
    {
        String url = fileService.userUploadFile(id, info, file);
        if (url == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.created(URI.create(url)).body(url);
    }


    @PostMapping("ref/{id}")
    public ResponseEntity<String> uploadRefFile(@PathVariable String id, @RequestBody UserFileRefUploadDto refUploadDto)
    {
        String url = fileService.userUploadRefFile(id, refUploadDto);
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
