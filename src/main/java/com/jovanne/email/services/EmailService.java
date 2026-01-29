package com.jovanne.email.services;

import com.jovanne.email.domain.EmailEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    private final JavaMailSender mailSender;
    private final EmailLogService emailLogService;
    private final EmailTemplateService emailTemplateService;

    @Override
    public void send(EmailEvent event, int attempt) {
        try {
            MimeMessage message = createMessage(event);
            mailSender.send(message);
            log.info(
                    "Email enviado com sucesso | eventId={} | to={}",
                    event.eventId(), event.to()
            );
            emailLogService.logSuccess(event, attempt);
        } catch (Exception ex) {
            log.error(
                    "Falha ao enviar o email | eventId={} | to={}",
                    event.eventId(), event.to()
            );
            emailLogService.logFailure(event, attempt, ex);
        }
    }

    private MimeMessage createMessage(EmailEvent event) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper =
                new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(event.to());
        helper.setSubject(event.subject());
        helper.setText(
                emailTemplateService.renderEmail(event),
                true
        );

        return message;
    }
}
