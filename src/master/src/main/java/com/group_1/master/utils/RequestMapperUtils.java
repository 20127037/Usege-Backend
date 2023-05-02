package com.group_1.master.utils;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

/**
 * com.group_1.master.utils
 * Created by NhatLinh - 19127652
 * Date 5/2/2023 - 7:34 PM
 * Description: ...
 */
public class RequestMapperUtils {
    public static Map<String, AttributeValue> mapPagingKey(Map<String, String> params)
    {
        Map<String, AttributeValue> attributeValueMap = null;
        if (params != null)
        {
            attributeValueMap = new HashMap<>();
            Map<String, AttributeValue> finalAttributeValueMap = attributeValueMap;
            params.forEach((k, v) -> finalAttributeValueMap.put(k, AttributeValue.fromS(v)));
        }
        return attributeValueMap;
    }
}
