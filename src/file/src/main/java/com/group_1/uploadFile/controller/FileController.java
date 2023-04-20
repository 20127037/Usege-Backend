package com.group_1.uploadFile.controller;

import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.uploadFile.service.FileService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@AllArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("{id}/file")
    public ResponseEntity<String> uploadFiles(@PathVariable("id") String userId, @RequestBody UserFile userFile) {

        //fileService.userUploadFile(userId,userFile);

        return ResponseEntity.ok("Upload File Successfully");
    }
}
