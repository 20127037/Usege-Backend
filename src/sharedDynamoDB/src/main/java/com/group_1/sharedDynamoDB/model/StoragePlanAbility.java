package com.group_1.sharedDynamoDB.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoragePlanAbility {
    private String description;
    private Boolean isCovered;
}