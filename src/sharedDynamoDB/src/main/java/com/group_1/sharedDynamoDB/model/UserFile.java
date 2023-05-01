package com.group_1.sharedDynamoDB.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
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
@FieldNameConstants
@AllArgsConstructor
public class UserFile {
    private String userId;
    private String fileName;
    private String contentType;
    private Long sizeInKb;
    private String updated;
    private List<String> tags;
    private String description;
    private String date;
    private String location;
    //uri to image in local
    private String originalUri;
    //uri to normal (bigger) version of image
    private String normalUri;
    //uri to tiny version of image
    private String tinyUri;
    private Boolean isFavourite;
    private Boolean isDeleted;
    private Integer remainingDays;
    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }
    @DynamoDbSortKey
    public String getUpdated() {
        return updated;
    }
    @DynamoDbSecondarySortKey(indexNames = "file-name-index")
    public String getFileName() {return fileName;}
    @DynamoDbSecondarySortKey(indexNames = "uri-index")
    public String getOriginalUri() {return originalUri;}
    @DynamoDbSecondarySortKey(indexNames = "favourite-index")
    public Boolean getFavourite() {
        return isFavourite;
    }
    @DynamoDbSecondarySortKey(indexNames = "deleted-index")
    public Boolean getDeleted() {
        return isDeleted;
    }
}