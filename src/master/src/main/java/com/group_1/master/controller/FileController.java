package com.group_1.master.controller;

import com.group_1.master.dto.LoadFleRequestDto;
import com.group_1.master.service.FileService;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

/**
 * com.group_1.master.controller
 * Created by NhatLinh - 19127652
 * Date 4/21/2023 - 4:23 PM
 * Description: ...
 */
@RestController("file")
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("{id}")
    public ResponseEntity<QueryResponse<UserFile>> getFiles(@PathVariable String id,
                                                            @RequestParam("limit") int limit,
                                                            @RequestParam(value = "favourite", required = false) Boolean favourite,
                                                            @RequestParam(value = "deleted", required = false) Boolean deleted,
                                                            @RequestBody LoadFleRequestDto requestDto)
    {
        Map<String, AttributeValue> attributeValueMap = null;
        if (requestDto.lastKey() != null)
        {
            attributeValueMap = new HashMap<>();
            Map<String, AttributeValue> finalAttributeValueMap = attributeValueMap;
            requestDto.lastKey().forEach((k, v) -> finalAttributeValueMap.put(k, AttributeValue.fromS(v)));
        }
        return ResponseEntity.ok(fileService.queryFiles(id, limit,
                favourite, deleted,
                attributeValueMap, requestDto.attributes()));
    }

    @GetMapping("{id}/check")
    public ResponseEntity<UserFile> getFile(@PathVariable String id,
                                                  @RequestParam(value = "fileName", required = false) String fileName,
                                                  @RequestParam(value = "uri", required = false) String uri)
    {
        return ResponseEntity.ok(fileService.getFile(id, fileName, uri));
    }
}
