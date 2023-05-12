package com.group_1.master.controller;

import com.group_1.master.dto.PagingRequestDto;
import com.group_1.master.service.TrashBinService;
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
 * Date 5/2/2023 - 7:23 PM
 * Description: ...
 */
@RestController
@RequestMapping("trash")
@AllArgsConstructor
@Tag(name = "Get trash bin", description = "Get user trash bin information")
public class TrashBinController {

    private final TrashBinService trashBinService;
    @GetMapping("{id}")
    @Operation(summary = "Get user files from the bin (paging)")
    public ResponseEntity<QueryResponse<UserFile>> getTrashFiles(@PathVariable String id,
                                                                 @RequestParam(value = "limit") int limit,
                                                                 @RequestParam(value = "lastKey", required = false) Map<String, String> lastKey)
    {
        return ResponseEntity.ok().body(trashBinService.queryImages(id, limit, RequestMapperUtils.mapPagingKey(lastKey)));
    }
    @GetMapping("{id}/{fileName}")
    @Operation(summary = "Get an user file from the bin by its name")
    public ResponseEntity<UserFile> getTrashFile(@PathVariable String id, @PathVariable String fileName)
    {
        return ResponseEntity.ok().body(trashBinService.getImage(id, fileName));
    }
}
