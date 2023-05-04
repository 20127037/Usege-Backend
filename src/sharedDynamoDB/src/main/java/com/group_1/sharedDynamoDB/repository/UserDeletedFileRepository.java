package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.model.UserFile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.HashMap;
import java.util.Map;

/**
 * com.group_1.sharedDynamoDB.repository
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 2:13 PM
 * Description: ...
 */
public class UserDeletedFileRepository extends DynamoDbRepository<UserFile> {
    public UserDeletedFileRepository(DynamoDbEnhancedClient client) {
        super(client.table("userDeletedFiles", TableSchema.fromBean(UserFile.class)));
    }

    @Override
    public Key getKeyFromItem(UserFile item) {
        return getKey(item.getUserId(), item.getFileName());
    }

    @Override
    public Map<String, String> getLastEvaluatedKeyFromItem(UserFile item) {
        HashMap<String, String> key = new HashMap<>();
        key.put(UserFile.Fields.userId, item.getUserId());
        key.put(UserFile.Fields.fileName, item.getFileName());
        key.put(UserFile.Fields.updated, item.getUpdated());
        return key;
    }
}

