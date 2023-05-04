package com.group_1.album.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * com.group_1.album.config
 * Created by NhatLinh - 19127652
 * Date 5/4/2023 - 3:56 PM
 * Description: ...
 */
@Configuration
@Data
public class RabbitMQConfig {

    @Value("${rabbitmq.exchanges.internal}")
    private String internalExchange;
    @Value("${rabbitmq.queue.album-add.name}")
    private String addAlbumQueue;
    @Value("${rabbitmq.queue.album-add.key}")
    private String internalAlbumAddKey;
    @Value("${rabbitmq.queue.album-del.name}")
    private String delAlbumQueue;
    @Value("${rabbitmq.queue.album-del.key}")
    private String internalAlbumDelKey;

    @Bean
    public TopicExchange internalTopicExchange()
    {
        return new TopicExchange(internalExchange);
    }
    @Bean
    public Declarables binding()
    {
        Queue addQueue = new Queue(addAlbumQueue);
        Queue delQueue = new Queue(delAlbumQueue);
        return new Declarables(
                addQueue,
                delQueue,
                internalTopicExchange(),
                BindingBuilder.bind(addQueue).to(internalTopicExchange()).with(internalAlbumAddKey),
                BindingBuilder.bind(delQueue).to(internalTopicExchange()).with(internalAlbumDelKey)
        );
    }
}
