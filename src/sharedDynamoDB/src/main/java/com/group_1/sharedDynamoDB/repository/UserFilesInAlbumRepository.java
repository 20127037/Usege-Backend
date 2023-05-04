package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.model.UserFile;
import com.group_1.sharedDynamoDB.model.UserFileInAlbum;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserFilesInAlbumRepository extends DynamoDbRepository<UserFileInAlbum> {
    public UserFilesInAlbumRepository(DynamoDbEnhancedClient client) {
        super(client.table("userFilesInAlbum", TableSchema.fromBean(UserFileInAlbum.class)));
    }

    @Override
    public Key getKeyFromItem(UserFileInAlbum item) {
        return getKey(item.getUserId(), item.getUpdated());
    }

    @Override
    public Map<String, String> getLastEvaluatedKeyFromItem(UserFileInAlbum item) {
        HashMap<String, String> key = new HashMap<>();
        key.put(UserFileInAlbum.Fields.userId, item.getUserId());
//        key.put(UserFileInAlbum.Fields.fileName, item.getFileName());
        key.put(UserFileInAlbum.Fields.albumName, item.getAlbumName());
        key.put(UserFileInAlbum.Fields.updated, item.getUpdated());
        return key;
    }
}
