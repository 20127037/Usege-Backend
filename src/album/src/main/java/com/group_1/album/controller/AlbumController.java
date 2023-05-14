package com.group_1.album.controller;

import com.group_1.album.service.AlbumService;
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

/**
 * com.group_1.master.controller
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 1:43 PM
 * Description: ...
 */
@RestController
@Tag(name = "Album", description = "Create/delete album, add/remove images to/from an album")
@AllArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @PostMapping("{id}/{name}")
    @Operation(summary = "Create a new album")
    public ResponseEntity<UserAlbum> createAlbum(HttpServletRequest request,
                                                 @PathVariable String id,
                                                 @PathVariable String name,
                                                 @RequestBody(required = false) String password) {
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(albumService.createAlbum(id, name, password));
    }

    @DeleteMapping("{id}/{name}")
    @Operation(summary = "Delete an album")
    public ResponseEntity<UserAlbum> deleteAlbum(@PathVariable String id, @PathVariable String name)
    {
        return ResponseEntity.ok().body(albumService.deleteAlbum(id, name));
    }

    @PutMapping("{id}/{name}")
    @Operation(summary = "Update an album")
    public ResponseEntity<UserAlbum> updateAlbum(@PathVariable String id, @PathVariable String name, @RequestBody UserAlbum update)
    {
        return ResponseEntity.ok().body(albumService.updateAlbum(id, name, update));
    }


    @PostMapping("{id}/{name}/images")
    @Operation(summary = "Add a list of user images to an album")
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
    @Operation(summary = "Delete a list of user images from an album")
    public ResponseEntity<List<UserFileInAlbum>> deleteFromAlbum(@PathVariable String id,
                                                 @PathVariable String name,
                                                 @RequestParam("file-names") String[] fileNames)
    {
        if (fileNames == null || fileNames.length == 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(albumService.deleteImagesFromAlbum(id, name, fileNames));
    }

    @PutMapping("{id}/{to}/images")
    @Operation(summary = "Move a list of user images from an album to another one")
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