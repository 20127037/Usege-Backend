package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.exception.NoSuchElementFoundException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedResponse;
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

import java.util.function.Consumer;

/**
 * dynamo.repository
 * Created by NhatLinh - 19127652
 * Date 3/22/2023 - 12:52 PM
 * Description: ...
 */
@AllArgsConstructor
public  class DynamoDbRepository<TValue> {
    protected final DynamoDbTable<TValue> table;
    public TValue saveRecord(TValue value) {
        PutItemEnhancedResponse<TValue> response = table.putItemWithResponse(b -> b.item(value));

        return response.attributes();
    }

    @SneakyThrows
    public TValue getRecordById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return table.getItem(builder -> builder.key(key));
    }

    public TValue deleteRecordById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return table.deleteItem(b -> b.key(key));
    }

    public TValue updateRecord(String id, Consumer<TValue> updateCallback)
    {
        TValue item = getRecordById(id);
        if (item == null)
            throw new NoSuchElementFoundException(id, table.tableName());
        updateCallback.accept(item);
        UpdateItemEnhancedResponse<TValue> response =  table.updateItemWithResponse(b -> b.item(item));
        return response.attributes();
    }
}
