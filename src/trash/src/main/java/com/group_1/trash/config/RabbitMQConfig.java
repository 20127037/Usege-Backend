package com.group_1.trash.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * com.group_1.album.config
 * Created by NhatLinh - 19127652
 * Date 5/4/2023 - 3:56 PM
 * Description: ...
 */
@Configuration
@Data
@AllArgsConstructor
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
}
