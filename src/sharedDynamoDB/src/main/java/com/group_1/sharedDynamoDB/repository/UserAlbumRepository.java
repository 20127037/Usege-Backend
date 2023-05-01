package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.model.UserAlbum;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class UserAlbumRepository extends DynamoDbRepository<UserAlbum> {
    public UserAlbumRepository(DynamoDbEnhancedClient client) {
        super(client.table("userAlbums", TableSchema.fromBean(UserAlbum.class)));
    }
}
