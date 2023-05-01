package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.exception.NoSuchElementFoundException;
import com.group_1.sharedDynamoDB.model.QueryResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;
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

    public TValue getRecordByKeyAndAttributes(Key key, boolean consistent)
    {
        return table
                .getItem(builder -> builder
                        .key(key)
                        .consistentRead(consistent));
    }

    @SneakyThrows
    public TValue getRecordById(String id) {
        return getRecordById(id, false);
    }
    @SneakyThrows
    public TValue getRecordById(String id, boolean consistent) {
        Key key = Key.builder().partitionValue(id).build();
        return getRecordByKey(key, consistent);
    }
    @SneakyThrows
    public TValue getRecordByIdAndSortKey(String id, String sortKey, boolean consistent) {
        Key key = Key.builder().partitionValue(id).sortValue(sortKey).build();
        return getRecordByKey(key, consistent);
    }

    public TValue getRecordByKey(Key key, boolean consistent)
    {
        return table.getItem(builder -> builder.key(key).consistentRead(consistent));
    }

    public TValue deleteRecordById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return table.deleteItem(b -> b.key(key));
    }

    public TValue updateRecord(String id, Consumer<TValue> updateCallback)
    {
        TValue item = getRecordById(id, true);
        if (item == null)
            throw new NoSuchElementFoundException(id, table.tableName());
        updateCallback.accept(item);
        UpdateItemEnhancedResponse<TValue> response =  table.updateItemWithResponse(b -> b.item(item));
        return response.attributes();
    }

    public QueryResponse<TValue> query(QueryConditional queryConditional,
                                       Expression expression,
                                       int limit,
                                       Map<String, AttributeValue> exclusiveStartKey,
                                       boolean forward,
                                       String... attributes) {
        PageIterable<TValue> response = table.query(b -> {
            b
                    .queryConditional(queryConditional)
                    .filterExpression(expression)
                    .limit(limit)
                    .exclusiveStartKey(exclusiveStartKey);
            if (attributes != null && attributes.length > 0)
                b.attributesToProject(attributes);
            b.scanIndexForward(forward);
        });

        Page<TValue> next = response.iterator().next();
        Map<String, AttributeValue> lastKey = next.lastEvaluatedKey();
        Map<String, String> lastKeyStrings = null;
        if (lastKey != null) {
            lastKeyStrings = new HashMap<>();
            Map<String, String> finalLastKeyStrings = lastKeyStrings;
            lastKey.forEach((k, v) -> finalLastKeyStrings.put(k, v.s()));
        }
        return QueryResponse.<TValue>builder()
                .response(next.items())
                .nextEvaluatedKey(lastKeyStrings)
                .build();
    }
}
