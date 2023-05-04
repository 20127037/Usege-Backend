package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.model.UserAlbum;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserAlbumRepository extends DynamoDbRepository<UserAlbum> {
    public UserAlbumRepository(DynamoDbEnhancedClient client) {
        super(client.table("userAlbums", TableSchema.fromBean(UserAlbum.class)));
    }

    @Override
    public Key getKeyFromItem(UserAlbum item) {
        return getKey(item.getUserId(), item.getName());
    }

    @Override
    public Map<String, String> getLastEvaluatedKeyFromItem(UserAlbum item) {
        HashMap<String, String> key = new HashMap<>();
        key.put(UserAlbum.Fields.userId, item.getUserId());
        key.put(UserAlbum.Fields.name, item.getName());
        return key;
    }
}
