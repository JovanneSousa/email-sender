package com.jovanne.email.consumer;

import com.jovanne.email.domain.EmailEvent;
import com.jovanne.email.services.IEmailService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
public class EmailConsumer {
    private final IEmailService service;

    @RabbitListener(
            queues = "${rabbit.email-queue}",
            containerFactory = "manualAckContainerFactory"
    )
    public void consume(
            EmailEvent event,
            Message message,
            Channel channel)throws IOException {

        int retryCount = (int) message.getMessageProperties()
                .getHeaders()
                .getOrDefault("x-retry-count", 0);

        try {
            service.send(event, retryCount);

            channel.basicAck(
                    message.getMessageProperties().getDeliveryTag(), false
            );
        } catch (Exception ex) {
            if (retryCount >= 3) {
                channel.basicReject(
                        message.getMessageProperties().getDeliveryTag(), false
                );
            } else {
                message.getMessageProperties()
                        .getHeaders()
                        .put("x-retry-count", retryCount + 1);
                channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,false
                );
            }
        }
    }
}
