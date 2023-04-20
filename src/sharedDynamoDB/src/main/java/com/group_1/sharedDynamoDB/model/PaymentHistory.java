package com.group_1.sharedDynamoDB.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

/**
 * com.group_1.sharedDynamoDB.model
 * Created by NhatLinh - 19127652
 * Date 4/20/2023 - 3:02 PM
 * Description: ...
 */
@Builder
@Data
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
public class PaymentHistory {

    private String userId;
    private String planName;
    private String purchasedDate;
    private String cardNumber;
    private String expiredDate;

    @DynamoDbPartitionKey
    public String getUserId()
    {
        return userId;
    }

    @DynamoDbSortKey
    public String getPlanName()
    {
        return planName;
    }
}
