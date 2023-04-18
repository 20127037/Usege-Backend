package com.group_1.sharedDynamoDB.model;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.attribute.DocumentAttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.attribute.MapAttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.util.List;

/**
 * model
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 5:47 PM
 * Description: ...
 */
@Builder
@Getter
@DynamoDbImmutable(builder = StoragePlan.StoragePlanBuilder.class)
public class StoragePlan {
    private String name;
    private Integer maximumSpaceInGB;
    private Float price;
    private String description;
    private int order;
    private List<StoragePlanAbility> abilities;
//    public List<StoragePlanAbility> getAbilities()
//    {
//        return abilities;
//    }
    @DynamoDbPartitionKey
    public String getName() {
        return name;
    }
    @DynamoDbSortKey
    public int getOrder() {return order;}
}


