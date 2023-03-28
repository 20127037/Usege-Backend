package com.group_1.sharedAws.model;

import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;

import java.time.LocalDateTime;

/**
 * model
 * Created by NhatLinh - 19127652
 * Date 3/24/2023 - 10:10 PM
 * Description: ...
 */
@Data
@Builder
@DynamoDbBean
public class UserInfo {
    private String userId;
    private String email;
    private Long storedFileCount;
    private Long usedSpace;
    private Long maxSpace;
    private String storagePackId;
    private LocalDateTime purchasedPackDate;
    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }
    @DynamoDbSecondaryPartitionKey(indexNames = "email-index")
    public String getEmail() {
        return email;
    }
}
