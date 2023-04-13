package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.model.StoragePackage;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * repository
 * Created by NhatLinh - 19127652
 * Date 3/21/2023 - 5:58 PM
 * Description: ...
 */
@Repository
public class StoragePackageRepository extends DynamoDbRepository<StoragePackage> {

    public StoragePackageRepository(DynamoDbEnhancedAsyncClient enhancedClient) {
        super(enhancedClient.table("storagePackages", TableSchema.fromBean(StoragePackage.class)));
    }
}