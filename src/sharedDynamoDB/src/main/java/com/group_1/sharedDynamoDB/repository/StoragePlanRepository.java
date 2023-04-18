package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.model.StoragePlan;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * repository
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 5:58 PM
 * Description: ...
 */
@Repository
public class StoragePlanRepository extends DynamoDbRepository<StoragePlan> {

    public StoragePlanRepository(DynamoDbEnhancedClient enhancedClient) {
        super(enhancedClient.table("storagePlans", TableSchema.fromImmutableClass(StoragePlan.class)));
    }

    public StoragePlan getBasicPlan()
    {
        return super.getRecordById("Basic");
    }

    public Iterable<StoragePlan> scanAll()
    {
        return table.scan().items();
    }
}