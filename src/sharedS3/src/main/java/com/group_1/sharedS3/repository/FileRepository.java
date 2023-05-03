package com.group_1.sharedS3.repository;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

/**
 * com.group_1.sharedS3.repository
 * Created by NhatLinh - 19127652
 * Date 4/12/2023 - 1:28 AM
 * Description: ...
 */
public interface FileRepository {
    void deleteFile(String folder, String fileName);
    ResponseInputStream<GetObjectResponse> getObject(String folder, String fileName);
    String uploadFile(String folder, String fileName, String contentType, byte[] content);
}