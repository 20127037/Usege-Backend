package com.group_1.sharedS3.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.nio.file.Path;


/**
 * com.group_1.sharedS3.repository
 * Created by NhatLinh - 19127652
 * Date 4/12/2023 - 1:30 AM
 * Description: ...
 */
@Repository
public class S3Repository implements FileRepository {
    private final S3Client s3Client;
    @Value("${amazon.aws.s3-bucket}")
    private String bucket;

    public S3Repository(S3Client s3Client) {
        this.s3Client = s3Client;
    }

//    public boolean folderExists(String name)
//    {
//        try {
//            GetObjectAttributesResponse response = s3Client.getObjectAttributes(b -> {
//                b.bucket(bucket).key(name);
//            });
//            return response != null;
//        } catch (NoSuchKeyException e) {
//            return false;
//        }
//    }
//    public void createFolder(String name)
//    {
//        s3Client.putObject(builder -> builder.bucket(folder)
//                .storageClass(StorageClass.ONEZONE_IA)
//                .acl(ObjectCannedACL.PUBLIC_READ_WRITE)
//                .key(fileName)
//                .contentType(contentType), RequestBody.fromBytes(content));
//    }
//    public void deleteFolder(String name)
//    {
//        s3Client.deleteBucket(b -> b.bucket(name));
//    }
    public void deleteFile(String folder, String fileName)
    {
        s3Client.deleteObject(builder -> builder.bucket(folder)
                .key(fileName));
    }
    public void uploadFile(String folder, String fileName, String contentType, byte[] content)
    {
        String key = String.format("%s/%s", folder, fileName);
        PutObjectResponse response = s3Client.putObject(builder -> builder
                .bucket(bucket)
                .storageClass(StorageClass.ONEZONE_IA)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .key(key)
                .contentType(contentType), RequestBody.fromBytes(content));
    }
}
