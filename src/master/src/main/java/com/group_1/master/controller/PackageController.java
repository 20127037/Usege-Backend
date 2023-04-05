package com.group_1.master.controller;

import com.group_1.master.service.StoragePackageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 5:56 PM
 * Description: ...
 */
@RestController("storagePackage")
@AllArgsConstructor
public class PackageController {

    private final StoragePackageService packageService;

//    @GetMapping
//    public ResponseEntity<Iterable<StoragePackage>> getAllPackages()
//    {
//        return ResponseEntity.ok().body(packageService.getAllPackages());
//    }
//
//    @GetMapping("{id}")
//    public ResponseEntity<StoragePackage> getPackage(@PathVariable String id)
//    {
//        return ResponseEntity.ok().body(packageService.getPackage(id));
//    }
}
