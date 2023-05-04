package com.group_1.amqp.configure;

import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * com.group_1.amqp
 * Created by NhatLinh - 19127652
 * Date 5/4/2023 - 3:46 PM
 * Description: ...
 */
@Configuration
@AllArgsConstructor
public class RabbitMQConfiguration {

    private final ConnectionFactory connectionFactory;

    @Bean
    public AmqpTemplate getAmqpTemplate()
    {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(getMessageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory getSimpleRabbitListenerContainerFactory()
    {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(getMessageConverter());
        return factory;
    }

    @Bean
    public MessageConverter getMessageConverter()
    {
        return new Jackson2JsonMessageConverter();
    }
}
