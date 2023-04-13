package com.group_1.master.service;


import com.group_1.sharedDynamoDB.model.StoragePackage;

/**
 * service
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 6:03 PM
 * Description: ...
 */
public interface StoragePackageService {
    Iterable<StoragePackage> getAllPackages();
    StoragePackage getPackage(String packageId);
}
