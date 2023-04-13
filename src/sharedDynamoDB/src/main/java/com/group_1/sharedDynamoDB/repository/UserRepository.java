package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.model.UserInfo;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * repository
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 10:28 PM
 * Description: ...
 */
@Repository
public class UserRepository extends DynamoDbRepository<UserInfo> {

    public UserRepository(DynamoDbEnhancedAsyncClient enhancedClient) {
        super(enhancedClient.table("userInfo", TableSchema.fromBean(UserInfo.class)));
    }
}
