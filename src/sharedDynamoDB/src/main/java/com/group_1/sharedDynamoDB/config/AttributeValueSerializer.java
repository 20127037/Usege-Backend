package com.group_1.sharedDynamoDB.config;

/**
 * com.group_1.sharedDynamoDB.config
 * Created by NhatLinh - 19127652
 * Date 4/21/2023 - 3:45 PM
 * Description: ...
 */
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.core.util.DefaultSdkAutoConstructList;
import software.amazon.awssdk.core.util.DefaultSdkAutoConstructMap;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * <p>Title: AttributeValueSerializer</p>
 * <p>Description: Jackson JSON serializer for the DynamoDB V2 SDK's {@link software.amazon.awssdk.services.dynamodb.model.AttributeValue}</p>
 * <p>Author: Whitehead</p>
 */
@JsonComponent
public class AttributeValueSerializer extends JsonSerializer<AttributeValue> {
    public static final DefaultSdkAutoConstructMap<?,?> EMPTY_ATTR_MAP = DefaultSdkAutoConstructMap.getInstance();
    public static final DefaultSdkAutoConstructList<?> EMPTY_ATTR_LIST = DefaultSdkAutoConstructList.getInstance();

    public static final JsonSerializer<AttributeValue> INSTANCE = new AttributeValueSerializer();

    private AttributeValueSerializer() {

    }

    @Override
    public Class<AttributeValue> handledType() {
        return AttributeValue.class;
    }

    @Override
    public void serialize(AttributeValue av, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (av == null) {
            gen.writeNull();
        } else {
            if (av.m() != EMPTY_ATTR_MAP) {
                gen.writeStartObject();
                Map<String, AttributeValue> map = av.m();
                for(Map.Entry<String, AttributeValue> entry : map.entrySet()) {
                    gen.writeFieldName(entry.getKey());
                    serialize(entry.getValue(), gen, serializers);
                }
                gen.writeEndObject();
            } else if (av.l() != EMPTY_ATTR_LIST) {
                List<AttributeValue> list = av.l();
                gen.writeStartArray();
                for(AttributeValue a : list) {
                    serialize(a, gen, serializers);
                }
                gen.writeEndArray();
            } else if (av.s() != null) {
                gen.writeString(av.s());
            } else if (av.n() != null) {
                gen.writeNumber(new BigDecimal(av.n()));
            } else if (av.bool() != null) {
                gen.writeBoolean(av.bool());
            } else if (av.nul() != null && av.nul()) {
                gen.writeNull();
            } else if (av.b() != null) {
                gen.writeBinary(av.b().asByteArray());
            } else if (av.ss() != EMPTY_ATTR_LIST) {
                List<String> list = av.ss();
                int size = list.size();
                gen.writeStartArray(size);
                for(int i = 0; i < size; i++) {
                    gen.writeString(list.get(i));
                }
                gen.writeEndArray();
            } else if (av.bs() != EMPTY_ATTR_LIST) {
                List<SdkBytes> list = av.bs();
                int size = list.size();
                gen.writeStartArray(size);
                for(int i = 0; i < size; i++) {
                    gen.writeBinary(list.get(i).asByteArray());
                }
                gen.writeEndArray();
            } else if (av.ns() != EMPTY_ATTR_LIST) {
                List<String> list = av.ns();
                int size = list.size();
                gen.writeStartArray(size);
                for(int i = 0; i < size; i++) {
                    gen.writeNumber(new BigDecimal(list.get(i)));
                }
                gen.writeEndArray();
            } else if (av.nul() != null) {
                gen.writeNull();
            }
//			else {
//				System.err.println("MISSED DATA TYPE: " + av);
//			}
        }
    }
}
