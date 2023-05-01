package com.group_1.sharedDynamoDB.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.List;
import java.util.Map;

/**
 * com.group_1.sharedDynamoDB.model
 * Created by NhatLinh - 19127652
 * Date 4/21/2023 - 3:01 PM
 * Description: ...
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueryResponse<S> {
    Map<String, String> nextEvaluatedKey;
    Map<String, String> prevEvaluatedKey;
    List<S> response;
}
