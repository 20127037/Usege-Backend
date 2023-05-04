package com.group_1.file.controller;

import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.file.dto.UserFileRefUploadDto;
import com.group_1.file.dto.UserFileUploadDto;
import com.group_1.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "File", description = "Upload/update user files")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "Upload a new file from user local storage")
    @PostMapping(value = "{id}", consumes = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<UserFile> uploadFiles(@PathVariable String id,
                                                @RequestPart("info") UserFileUploadDto info,
                                                @RequestPart("file") MultipartFile file)
    {
        UserFile created = fileService.userUploadFile(id, info, file);
        if (created == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.created(URI.create(created.getNormalUri())).body(created);
    }


    @PutMapping("{id}")
    @Operation(summary = "Update file possible information (favourite/tags/description/location)")
    public ResponseEntity<UserFile> updateFile(@PathVariable String id,
                                               @RequestBody UserFile updated)
    {
        return ResponseEntity.ok().body(fileService.updateFile(id, updated));
    }

    @PostMapping("ref/{id}")
    @Operation(summary = "Upload a new file from a 3rd library")
    public ResponseEntity<UserFile> uploadRefFile(@PathVariable String id, @RequestBody UserFileRefUploadDto refUploadDto)
    {
        UserFile created = fileService.userUploadRefFile(id, refUploadDto);
        if (created == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.created(URI.create(created.getNormalUri())).body(created);
    }
}
