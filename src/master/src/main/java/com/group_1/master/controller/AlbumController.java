package com.group_1.master.controller;

import com.group_1.master.dto.PagingRequestDto;
import com.group_1.master.dto.QueryFilesInAlbumResponse;
import com.group_1.master.service.AlbumService;
import com.group_1.master.utils.RequestMapperUtils;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserAlbum;
import com.group_1.sharedDynamoDB.model.UserFileInAlbum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * com.group_1.master.controller
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 1:43 PM
 * Description: ...
 */
@RestController
@RequestMapping("album")
@AllArgsConstructor
@Tag(name = "Get albums", description = "Get user albums information")
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping("{id}/{name}/images")
    @Operation(summary = "Get user files from an album (paging)")
    public ResponseEntity<QueryFilesInAlbumResponse> getFilesFromAlbum(@PathVariable String id,
                                                                       @PathVariable String name,
                                                                       @RequestParam(value = "limit") int limit,
                                                                       @RequestParam(value = "lastKey", required = false) Map<String, String> lastKey)
    {
        return ResponseEntity.ok().body(albumService.queryImages(id, name, limit, RequestMapperUtils.mapPagingKey(lastKey)));
    }
    @GetMapping("{id}")
    @Operation(summary = "Get user albums (paging)")
    public ResponseEntity<QueryResponse<UserAlbum>> getAlbums(@PathVariable String id,
                                                              @RequestParam(value = "limit") int limit,
                                                              @RequestParam(value = "lastKey", required = false) Map<String, String> lastKey)
    {
        return ResponseEntity.ok().body(albumService.queryAlbums(id, limit, RequestMapperUtils.mapPagingKey(lastKey)));
    }
}