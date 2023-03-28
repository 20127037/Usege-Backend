package com.group_1.sharedAws.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.List;

/**
 * model
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 5:47 PM
 * Description: ...
 */
@Data
@Builder
@DynamoDbBean
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class StoragePackage {
    private String storagePackId;
    private String packName;
    private Integer maximumSpaceInGB;
    private Double price;
    private String description;
    private List<String> openedFeatures;
    private String parentPacks;
    @DynamoDbPartitionKey
    public String getStoragePackId() {
        return storagePackId;
    }
}
