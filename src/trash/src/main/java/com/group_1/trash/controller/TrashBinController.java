package com.group_1.trash.controller;

import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.trash.service.TrashBinService;
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
 * Date 5/2/2023 - 7:23 PM
 * Description: ...
 */
@RestController
@AllArgsConstructor
@Tag(name = "Trash bin", description = "Create/delete/restore user deleted files")
public class TrashBinController {

    private final TrashBinService trashBinService;
    @PostMapping("{id}")
    @Operation(summary = "Move a file to the trash bin")
    public ResponseEntity<List<UserFile>> deleteFile(HttpServletRequest request,
                                                     @PathVariable String id,
                                                     @RequestParam("file-names") String[] fileNames) {
        if (fileNames == null || fileNames.length == 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(trashBinService.createDeletedFiles(id, fileNames));
    }

    @PostMapping("{id}/all")
    @Operation(summary = "Move all user files to the trash bin")
    public ResponseEntity<List<UserFile>> deleteAllFiles(HttpServletRequest request,
                                                     @PathVariable String id) {
        return ResponseEntity.created(URI.create(request.getRequestURI())).body(trashBinService.createDeletedFilesFromAllFiles(id));
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Clear a list of files in the trash bin using file names")
    public ResponseEntity<List<UserFile>> clearDeletedFiles(@PathVariable String id,
                                                            @RequestParam("file-names") String[] fileNames)
    {
        if (fileNames == null || fileNames.length == 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(trashBinService.clearDeletedFiles(id, fileNames));
    }
    @DeleteMapping("{id}/all")
    @Operation(summary = "Clear all files in the trash")
    public ResponseEntity<List<UserFile>> clearAllFiles(@PathVariable String id)
    {
        return ResponseEntity.ok().body(trashBinService.clearAll(id));
    }
    @PutMapping("{id}")
    @Operation(summary = "Restore a list of files in the trash bin using file names")
    public ResponseEntity<List<UserFile>> restoreFiles(@PathVariable String id,
                                                       @RequestParam("file-names") String[] fileNames)
    {
        if (fileNames == null || fileNames.length == 0)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok().body(trashBinService.restoreFiles(id, fileNames));
    }
    @PutMapping("{id}/all")
    @Operation(summary = "Restore all files in the trash bin")
    public ResponseEntity<List<UserFile>> restoreAllFiles(@PathVariable String id)
    {
        return ResponseEntity.ok().body(trashBinService.restoreAll(id));
    }
}
