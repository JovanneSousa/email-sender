package com.jovanne.email.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitConfig {

    private final RabbitProperties props;

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    TopicExchange emailExchange() {
        return new TopicExchange(props.getEmailExchange());
    }
    @Bean
    DirectExchange emailRetryExchange() {
        return new DirectExchange(props.getEmailRetryExchange());
    }

    @Bean
    DirectExchange emailDlqExchange() {
        return new DirectExchange(props.getEmailDlx());
    }

    @Bean
    Queue emailRetryQueue() {
        return QueueBuilder.durable(props.getEmailRetryQueue())
                .ttl(5000)
                .deadLetterExchange(props.getEmailExchange())
                .deadLetterRoutingKey(props.getEmailRoutingKey())
                .build();
    }

    @Bean
    Queue emailQueue() {
        return QueueBuilder.durable(props.getEmailQueue())
                .deadLetterExchange(props.getEmailRetryExchange())
                .deadLetterRoutingKey(props.getEmailRetryRoutingKey())
                .build();
    }

    @Bean
    Queue emailDlq() {
        return QueueBuilder.durable(props.getEmailDlq()).build();
    }

    @Bean
    Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(emailExchange())
                .with(props.getEmailRoutingKey());
    }

    @Bean
    Binding retryBinding() {
        return BindingBuilder
                .bind(emailRetryQueue())
                .to(emailRetryExchange())
                .with(props.getEmailRetryRoutingKey());
    }

    @Bean
    Binding emailDqlBinding() {
        return BindingBuilder
                .bind(emailDlq())
                .to(emailDlqExchange())
                .with(props.getEmailDlqRoutingKey());
    }

    @Bean
    public JacksonJsonMessageConverter jackson2JsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory manualAckContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory =
                new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        return factory;
    }
}
