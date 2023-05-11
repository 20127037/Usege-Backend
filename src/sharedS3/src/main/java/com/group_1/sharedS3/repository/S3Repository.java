package com.group_1.sharedS3.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.ResponseInputStream;
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
    private final String bucket;

    public S3Repository(S3Client s3Client,
                        @Value("${amazon.aws.s3.bucket}") String bucket) {
        this.s3Client = s3Client;
        this.bucket = bucket;
    }

    public void deleteFile(String folder, String fileName)
    {
        s3Client.deleteObject(builder -> builder.bucket(folder)
                .key(fileName));
    }
    public ResponseInputStream<GetObjectResponse> getObject(String folder, String fileName)
    {
        String key = String.format("%s/%s", folder, fileName);
        try
        {
            return s3Client.getObject(GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public String uploadFile(String folder, String fileName, String contentType, byte[] content)
    {
        String key = String.format("%s/%s", folder, fileName);
        PutObjectResponse response = s3Client.putObject(builder -> builder
                .bucket(bucket)
                .storageClass(StorageClass.ONEZONE_IA)
                .acl(ObjectCannedACL.PUBLIC_READ)
                .key(key)
                .contentType(contentType), RequestBody.fromBytes(content));
        if (response.sdkHttpResponse().isSuccessful())
            return s3Client.utilities().getUrl(b -> b.bucket(bucket).key(key)).toExternalForm();
        return null;
    }
}
