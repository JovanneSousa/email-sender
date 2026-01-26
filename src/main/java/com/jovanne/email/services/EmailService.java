package com.jovanne.email.services;

import com.jovanne.email.domain.EmailEvent;
import com.jovanne.email.exceptions.EmailSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService{
    private final JavaMailSender mailSender;
    private final EmailLogService emailLogService;

    @Override
    public void send(EmailEvent event) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.to());
            message.setSubject(event.subject());
            message.setText(event.body());

            mailSender.send(message);
            log.info(
                    "Email enviado com sucesso | eventId={} | to={}",
                    event.eventId(), event.to()
            );
            emailLogService.logSuccess(event, 1);
        } catch (Exception ex) {
            log.error(
                    "Falha ao enviar email | eventId={} | to={}",
                    event.eventId(), event.to()
            );

            emailLogService.logFailure(event, 3, ex);
            throw new EmailSendException("Erro ao enviar email", ex);
        }
    }
}
