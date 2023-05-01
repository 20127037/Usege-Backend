package com.group_1.sharedDynamoDB.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@Builder
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public class UserFileInAlbum {
    private String userId;
    private String updated;
    private String albumName;
    private String fileName;
    private String normalUri;
    private String tinyUri;
    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }
    @DynamoDbSecondarySortKey(indexNames = "album-index")
    public String getAlbumName() { return albumName; }
    @DynamoDbSortKey
    public String getUpdated() {
        return updated;
    }
}