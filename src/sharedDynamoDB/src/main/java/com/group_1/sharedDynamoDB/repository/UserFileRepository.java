package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.model.UserFile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * com.group_1.sharedAws.repository
 * Created by NhatLinh - 19127652
 * Date 4/12/2023 - 1:02 AM
 * Description: ...
 */
@Repository
public class UserFileRepository extends DynamoDbRepository<UserFile>{
    public UserFileRepository(DynamoDbEnhancedClient asyncClient) {
        super(asyncClient.table("userFiles", TableSchema.fromBean(UserFile.class)));
    }

    @Override
    public Key getKeyFromItem(UserFile item) {
        return getKey(item.getUserId(), item.getFileName());
    }
}
