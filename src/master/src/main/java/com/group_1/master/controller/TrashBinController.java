package com.group_1.master.controller;

import com.group_1.master.dto.PagingRequestDto;
import com.group_1.master.service.TrashBinService;
import com.group_1.master.utils.RequestMapperUtils;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * com.group_1.master.controller
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 7:23 PM
 * Description: ...
 */
@RestController
@RequestMapping("trash")
@AllArgsConstructor
public class TrashBinController {

    private final TrashBinService trashBinService;
    @GetMapping("{id}")
    public ResponseEntity<QueryResponse<UserFile>> getTrashFiles(@PathVariable String id,
                                                              @RequestBody PagingRequestDto requestDto)
    {
        return ResponseEntity.ok().body(trashBinService.queryImages(id, requestDto.limit(), RequestMapperUtils.mapPagingKey(requestDto.lastKey())));
    }
    @GetMapping("{id}/{fileName}")
    public ResponseEntity<UserFile> getTrashFile(@PathVariable String id, @PathVariable String fileName)
    {
        return ResponseEntity.ok().body(trashBinService.getImage(id, fileName));
    }
}
