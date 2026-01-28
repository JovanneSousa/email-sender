package com.jovanne.email.consumer;

import com.jovanne.email.domain.EmailEvent;
import com.jovanne.email.services.EmailLogService;
import com.jovanne.email.services.EmailService;
import com.jovanne.email.services.ResendService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailConsumer {
    private final ResendService service;

    @RabbitListener(queues = "${rabbit.email-queue}", containerFactory = "manualAckContainerFactory")
    public void consume(
            EmailEvent event,
            Message message,
            Channel channel)throws IOException {
        int retryCount = (int) message.getMessageProperties()
                .getHeaders()
                .getOrDefault("x-retry-count", 0);

        try {
            service.sendEmail(event, retryCount);

            channel.basicAck(
                    message.getMessageProperties().getDeliveryTag(), false
            );
        } catch (Exception ex) {
            log.error(
                    "Falha ao enviar o email | eventId={} | tentativa={} ",
                    event.eventId(), retryCount + 1, ex
            );

            if (retryCount >= 3) {
                channel.basicReject(
                        message.getMessageProperties().getDeliveryTag(), false
                );
            } else {
                channel.basicNack(
                        message.getMessageProperties().getDeliveryTag(),
                        false,false
                );
            }
        }
    }
}
