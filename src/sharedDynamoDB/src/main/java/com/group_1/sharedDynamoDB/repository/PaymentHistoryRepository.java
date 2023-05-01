package com.group_1.sharedDynamoDB.repository;

import com.group_1.sharedDynamoDB.model.PaymentHistory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

/**
 * com.group_1.sharedDynamoDB.repository
 * Created by NhatLinh - 19127652
 * Date 4/20/2023 - 3:05 PM
 * Description: ...
 */
@Repository
public class PaymentHistoryRepository extends DynamoDbRepository<PaymentHistory> {

    public PaymentHistoryRepository(DynamoDbEnhancedClient client) {
        super(client.table("paymentHistories", TableSchema.fromBean(PaymentHistory.class)));
    }
}

