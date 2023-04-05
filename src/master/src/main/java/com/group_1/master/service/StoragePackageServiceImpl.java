package com.group_1.master.service;

import com.group_1.sharedAws.model.StoragePackage;
import com.group_1.sharedAws.repository.StoragePackageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * service
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 6:03 PM
 * Description: ...
 */
@Service
@AllArgsConstructor
public class StoragePackageServiceImpl implements StoragePackageService {
    private final StoragePackageRepository repository;
    @Override
    public Iterable<StoragePackage> getAllPackages() {
        return null;
        //return repository.findAll();
    }
    @Override
    public StoragePackage getPackage(String packageId) {
        return null;
//        Optional<StoragePackage> storagePackage = repository.getRecordById(packageId);
//        if (storagePackage.isEmpty())
//            throw new NoSuchElementFoundException(packageId, "storage packages");
//        return storagePackage.get();
    }
}
