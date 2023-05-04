package com.group_1.trash.service;

import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import lombok.NonNull;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

/**
 * com.group_1.master.service
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 2:08 PM
 * Description: ...
 */
public interface TrashBinService {
    List<UserFile> createDeletedFiles(String userId, @NonNull String... fileNames);
    List<UserFile> createDeletedFilesFromAllFiles(String userId);
    List<UserFile> clearDeletedFiles(String userId, String... fileNames);
    List<UserFile> clearAll(String userId);
    List<UserFile> restoreFiles(String userId, String... fileNames);
    List<UserFile> restoreAll(String userId);
}