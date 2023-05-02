package com.group_1.sharedDynamoDB.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondarySortKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@Builder
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class UserFileInAlbum {
    private String userId;
    private String updated;
    private String albumName;
    private String fileName;
    public interface Indexes
    {
        String ALBUM_NAME = "album-name-index";
        String FILE_NAME_INDEX = "file-name-index";
    }
    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }
    @DynamoDbSecondarySortKey(indexNames = Indexes.ALBUM_NAME)
    public String getAlbumName() { return albumName; }
    @DynamoDbSecondarySortKey(indexNames = Indexes.FILE_NAME_INDEX)
    public String getFileName() { return fileName; }
    @DynamoDbSortKey
    public String getUpdated() {
        return updated;
    }
}