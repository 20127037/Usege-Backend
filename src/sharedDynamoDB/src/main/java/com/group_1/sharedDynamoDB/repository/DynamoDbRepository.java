package com.group_1.sharedDynamoDB.repository;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedResponse;

/**
 * dynamo.repository
 * Created by NhatLinh - 19127652
 * Date 3/22/2023 - 12:52 PM
 * Description: ...
 */
@AllArgsConstructor
public  class DynamoDbRepository<TValue> {
    private final DynamoDbAsyncTable<TValue> table;
    public void saveRecord(TValue value) {
        table.putItem(b -> b.item(value));
    }

    @SneakyThrows
    public TValue getRecordById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return table.getItem(builder -> builder.key(key)).getNow(null);
    }

    public TValue deleteRecordById(String id) {
        Key key = Key.builder().partitionValue(id).build();
        return table.deleteItem(b -> b.key(key)).getNow(null);
    }
//
//    public TValue updateCustomer(TValue update) {
////        HashMap<String, AttributeValueUpdate> updatedValues = new HashMap<>();
////        for (Map.Entry<String, TValue> entry : updateValues)
////        {
////            updatedValues.put(entry.getKey(), AttributeValueUpdate.builder()
////                    .value(AttributeValue.builder().s(entry.getValue()).build())
////                    .action(AttributeAction.PUT)
////                    .build());
////        }
//        return table.updateItem(builder -> {
//            builder.item(update);
//        });
//    }
}
