package com.jovanne.email.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE = "email.exchange";
    public static final String QUEUE = "email.queue";

    @Bean
    TopicExchange emailExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    Queue emailQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }

    @Bean
    Binding binding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(emailExchange())
                .with("email.#");
    }

    @Bean
    public JacksonJsonMessageConverter jackson2JsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
