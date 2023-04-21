package com.group_1.sharedDynamoDB.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.List;

/**
 * com.group_1.sharedAws.model
 * Created by NhatLinh - 19127652
 * Date 4/12/2023 - 12:54 AM
 * Description: ...
 */
@Data
@Builder
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public class UserFile {
    private String userId;
    private String fileName;
    private String contentType;
    private Long sizeInKb;
    private String updated;
    private String fileUrl;
    private List<String> tags;
    private String description;
    private String date;
    private String location;
    private String uriLocal;
    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }
    @DynamoDbSortKey
    public String getUpdated() {
        return updated;
    }
    @DynamoDbSecondarySortKey(indexNames = "content-type-index")
    public String getContentType() {return contentType;}
    @DynamoDbSecondaryPartitionKey(indexNames = "uri-local-index")
    public String getUriLocal() {return uriLocal;}
}