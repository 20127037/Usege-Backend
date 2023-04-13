package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.model.UserFile;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * com.group_1.sharedAws.repository
 * Created by NhatLinh - 19127652
 * Date 4/12/2023 - 1:02 AM
 * Description: ...
 */
@Repository
public class UserFileDbRepository extends DynamoDbRepository<UserFile>{
    public UserFileDbRepository(DynamoDbEnhancedAsyncClient asyncClient) {
        super(asyncClient.table("userFiles", TableSchema.fromBean(UserFile.class)));
    }
}
