package com.group_1.trash.producers;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Component;

/**
 * com.group_1.amqp.producers
 * Created by NhatLinh - 19127652
 * Date 5/4/2023 - 4:13 PM
 * Description: ...
 */
@Component
@Slf4j
@AllArgsConstructor
public class RabbitMQMessageProducer {

    private final AmqpTemplate amqpTemplate;

    public <S> void publish(S payload, String exchange, String routingKey)
    {
        log.info("Publishing to {} using key {}. Payload: {}", exchange, routingKey, payload);
        amqpTemplate.convertAndSend(exchange, routingKey, payload);
    }
}
