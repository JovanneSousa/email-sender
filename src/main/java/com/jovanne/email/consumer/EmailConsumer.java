package com.jovanne.email.consumer;

import com.jovanne.email.config.RabbitConfig;
import com.jovanne.email.domain.EmailEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailConsumer {

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void consume(EmailEvent event){
        log.info(
                "Evento recebido | id={} | type={} | to={}",
                event.eventId(), event.type(), event.to()
        );
    }
}
