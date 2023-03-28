package service;

import com.group_1.sharedAws.model.StoragePackage;

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
