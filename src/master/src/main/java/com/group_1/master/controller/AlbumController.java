package com.group_1.master.controller;

import com.group_1.master.dto.PagingRequestDto;
import com.group_1.master.dto.QueryFilesInAlbumResponse;
import com.group_1.master.service.AlbumService;
import com.group_1.master.utils.RequestMapperUtils;
import com.group_1.sharedDynamoDB.model.UserAlbum;
import com.group_1.sharedDynamoDB.model.UserFileInAlbum;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;

/**
 * com.group_1.master.controller
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 1:43 PM
 * Description: ...
 */
@RestController("album")
@AllArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping("{id}/{name}")
    public ResponseEntity<UserAlbum> createAlbum(HttpServletRequest request, @PathVariable String id, @PathVariable String name) {
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(albumService.createAlbum(id, name));
    }

    @DeleteMapping("{id}/{name}")
    public ResponseEntity<UserAlbum> deleteAlbum(@PathVariable String id, @PathVariable String name)
    {
        return ResponseEntity.ok().body(albumService.deleteAlbum(id, name));
    }

    @GetMapping("{id}/{name}/images")
    public ResponseEntity<QueryFilesInAlbumResponse> getFiles(@PathVariable String id,
                                                              @PathVariable String name,
                                                              @RequestBody PagingRequestDto requestDto)
    {
        return ResponseEntity.ok().body(albumService.queryImages(id, name, requestDto.limit(), RequestMapperUtils.mapPagingKey(requestDto.lastKey())));
    }

    @PostMapping("{id}/{name}/images")
    public ResponseEntity<List<UserFileInAlbum>> addToAlbum(HttpServletRequest request,
                                                @PathVariable String id,
                                                 @PathVariable String name,
                                                 @RequestParam("file-names") String[] fileNames)
    {
        if (fileNames == null || fileNames.length == 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(albumService.addImagesToAlbum(id, name, fileNames));
    }

    @DeleteMapping("{id}/{name}/images")
    public ResponseEntity<List<UserFileInAlbum>> deleteFromAlbum(@PathVariable String id,
                                                 @PathVariable String name,
                                                 @RequestParam("file-names") String[] fileNames)
    {
        if (fileNames == null || fileNames.length == 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(albumService.deleteImagesFromAlbum(id, name, fileNames));
    }

    @PutMapping("{id}/{to}/images")
    public ResponseEntity<List<UserFileInAlbum>> moveFromAlbumToAlbum(@PathVariable String id,
                                                     @PathVariable String to,
                                                     @RequestParam("from") String from,
                                                     @RequestParam("file-names") String[] fileNames)
    {
        if (fileNames == null || fileNames.length == 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(albumService.moveImages(id, from, to, fileNames));
    }
}