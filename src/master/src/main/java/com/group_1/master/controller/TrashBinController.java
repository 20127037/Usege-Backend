package com.group_1.master.controller;

import com.group_1.master.dto.PagingRequestDto;
import com.group_1.master.service.TrashBinService;
import com.group_1.master.utils.RequestMapperUtils;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

/**
 * com.group_1.master.controller
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 7:23 PM
 * Description: ...
 */
@RestController("trash")
@AllArgsConstructor
public class TrashBinController {

    private final TrashBinService trashBinService;
    @PostMapping("{id}")
    public ResponseEntity<List<UserFile>> deleteFile(HttpServletRequest request,
                                                     @PathVariable String id,
                                                     @RequestParam("file-names") String[] fileNames) {
        if (fileNames == null || fileNames.length == 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(trashBinService.createDeletedFiles(id, fileNames));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<List<UserFile>> clearDeletedFiles(@PathVariable String id,
                                                            @RequestParam("file-names") String[] fileNames)
    {
        if (fileNames == null || fileNames.length == 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(trashBinService.clearDeletedFiles(id, fileNames));
    }
    @DeleteMapping("{id}/all")
    public ResponseEntity<List<UserFile>> clearAll(@PathVariable String id)
    {
        return ResponseEntity.ok().body(trashBinService.clearAll(id));
    }
    @PutMapping("{id}")
    public ResponseEntity<List<UserFile>> restoreFiles(@PathVariable String id,
                                                       @RequestParam("file-names") String[] fileNames)
    {
        if (fileNames == null || fileNames.length == 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(trashBinService.restoreFiles(id, fileNames));
    }
    @PutMapping("{id}/all")
    public ResponseEntity<List<UserFile>> restoreAllFiles(@PathVariable String id)
    {
        return ResponseEntity.ok().body(trashBinService.restoreAll(id));
    }

    @GetMapping("{id}")
    public ResponseEntity<QueryResponse<UserFile>> getFiles(@PathVariable String id,
                                                              @RequestBody PagingRequestDto requestDto)
    {

        return ResponseEntity.ok().body(trashBinService.queryImages(id, requestDto.limit(), RequestMapperUtils.mapPagingKey(requestDto.lastKey())));
    }
}
