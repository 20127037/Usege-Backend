package com.group_1.sharedDynamoDB.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.extensions.annotations.DynamoDbAtomicCounter;
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
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private String userId;
    private String email;
    //Use kb unit
    private Long imgCount;
    private Long albumCount;
    private Long usedSpace;
    private Long maxSpace;
    private String plan;
    private int planOrder;
    private String purchasedPlanDate;
    @DynamoDbAtomicCounter
    public Long getUsedSpace() {return usedSpace;}
    @DynamoDbAtomicCounter
    public Long getImgCount() {return imgCount;}
    @DynamoDbAtomicCounter
    public Long getAlbumCount() {return imgCount;}
    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }
    @DynamoDbSecondaryPartitionKey(indexNames = "email-index")
    public String getEmail() {
        return email;
    }
}
