package com.group_1.master.controller;

import com.group_1.master.dto.PagingRequestDto;
import com.group_1.master.service.FileService;
import com.group_1.master.utils.RequestMapperUtils;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * com.group_1.master.controller
 * Created by NhatLinh - 19127652
 * Date 4/21/2023 - 4:23 PM
 * Description: ...
 */
@RestController
@RequestMapping("file")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("{id}")
    public ResponseEntity<QueryResponse<UserFile>> getFiles(@PathVariable String id,
                                                            @RequestParam(value = "favourite", required = false) Boolean favourite,
                                                            @RequestBody PagingRequestDto requestDto)
    {
        return ResponseEntity.ok(fileService.queryFiles(id, requestDto.limit(),
                favourite, RequestMapperUtils.mapPagingKey(requestDto.lastKey()), requestDto.attributes()));
    }


    @GetMapping("{id}/{fileName}")
    public ResponseEntity<UserFile> getFile(@PathVariable String id,
                                            @PathVariable String fileName,
                                            @RequestParam(value = "trash-include", required = false, defaultValue = "true") Boolean trash)
    {
        if (fileName != null)
            return ResponseEntity.ok(fileService.getFileByName(id, fileName, trash));
//        if (uri != null)
//            return ResponseEntity.ok(fileService.getFileByOriginalUri(id, uri));
        return ResponseEntity.badRequest().build();
    }
}
