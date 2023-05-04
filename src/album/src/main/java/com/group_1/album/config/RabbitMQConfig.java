package com.group_1.album.config;

import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
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
    public SimpleRabbitListenerContainerFactory getSimpleRabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, MessageConverter messageConverter
    )
    {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
    @Bean
    Binding binding1(TopicExchange exchange) {
        return BindingBuilder.bind(new Queue(addAlbumQueue)).to(exchange).with(internalAlbumAddKey);
    }

    @Bean
    Binding binding2(TopicExchange exchange) {
        return BindingBuilder.bind(new Queue(delAlbumQueue)).to(exchange).with(internalAlbumDelKey);
    }
}
