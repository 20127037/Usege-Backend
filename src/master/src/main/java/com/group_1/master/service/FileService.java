package com.group_1.master.service;

import com.group_1.sharedDynamoDB.model.QueryResponse;
import com.group_1.sharedDynamoDB.model.UserFile;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

/**
 * com.group_1.master.service
 * Created by NhatLinh - 19127652
 * Date 4/21/2023 - 4:21 PM
 * Description: ...
 */
public interface FileService {
    QueryResponse<UserFile> queryFiles(String userId, int limit,
                                              Boolean isFavourite,
                                              Map<String, AttributeValue> startKey,
                                              String[] attributes);
    UserFile getFileByName(String userId, String fileName, boolean deletedInclude);
    UserFile updateFile(String userId, UserFile update);
//    UserFile deleteFile(String userId, String fileName);
}
