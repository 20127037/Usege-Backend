package com.group_1.sharedS3.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.StorageClass;


/**
 * com.group_1.sharedS3.repository
 * Created by NhatLinh - 19127652
 * Date 4/12/2023 - 1:30 AM
 * Description: ...
 */
@Repository
@AllArgsConstructor
public class S3Repository implements FileRepository {
    private final S3Client s3Client;
    public boolean folderExists(String name)
    {
        try {
            s3Client.headBucket(b -> b.bucket(name));
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }
    }
    public void createFolder(String name)
    {
        s3Client.createBucket(b -> b.bucket(name));
    }
    public void deleteFolder(String name)
    {
        s3Client.deleteBucket(b -> b.bucket(name));
    }
    public void deleteFile(String folder, String fileName)
    {
        s3Client.deleteObject(builder -> builder.bucket(folder)
                .key(fileName));
    }
    public void uploadFile(String folder, String fileName, String contentType, byte[] content)
    {
        s3Client.putObject(builder -> builder.bucket(folder)
                .storageClass(StorageClass.ONEZONE_IA)
                .key(fileName)
                .contentType(contentType), RequestBody.fromBytes(content));
    }
}
