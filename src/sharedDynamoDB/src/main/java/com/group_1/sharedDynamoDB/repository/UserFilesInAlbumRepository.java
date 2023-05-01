package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.model.UserFileInAlbum;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class UserFilesInAlbumRepository extends DynamoDbRepository<UserFileInAlbum> {
    public UserFilesInAlbumRepository(DynamoDbEnhancedClient client) {
        super(client.table("userFilesInAlbum", TableSchema.fromBean(UserFileInAlbum.class)));
    }
}
