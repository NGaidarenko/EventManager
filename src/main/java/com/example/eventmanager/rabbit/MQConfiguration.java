package com.example.eventmanager.rabbit;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQConfiguration {
    static final String queueName = "firstQueue";
    static final String exchangeName = "testExchange";

    @Bean
    public Queue myQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    FanoutExchange myExchange() {
        return new FanoutExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue myQueue, FanoutExchange exchangeName) {
        return BindingBuilder.bind(myQueue).to(exchangeName);
    }

}
