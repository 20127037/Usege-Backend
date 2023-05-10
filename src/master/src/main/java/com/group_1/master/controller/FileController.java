package com.group_1.master.controller;

import com.group_1.master.dto.PagingRequestDto;
import com.group_1.master.service.FileService;
import com.group_1.master.utils.RequestMapperUtils;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * com.group_1.master.controller
 * Created by NhatLinh - 19127652
 * Date 4/21/2023 - 4:23 PM
 * Description: ...
 */
@RestController
@RequestMapping("file")
@AllArgsConstructor
@Tag(name = "Get files", description = "Get user files information")
public class FileController {

    private final FileService fileService;

    @GetMapping("{id}")
    @Operation(summary = "Get user files (paging)")
    public ResponseEntity<QueryResponse<UserFile>> getFiles(@PathVariable String id,
                                                            @RequestParam(value = "favourite", required = false) Boolean favourite,
                                                            @RequestParam(value = "limit") int limit,
                                                            @RequestParam(value = "attributes", required = false) String[] attributes,
                                                            @RequestParam(value = "lastKey", required = false) Map<String, String> lastKey)
    {
        return ResponseEntity.ok(fileService.queryFiles(id, limit,
                favourite, RequestMapperUtils.mapPagingKey(lastKey), attributes));
    }


    @GetMapping("{id}/{fileName}")
    @Operation(summary = "Get a user file from its name")
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
