package com.jovanne.email.services;

import com.jovanne.email.domain.EmailEvent;
import com.jovanne.email.exceptions.EmailSendException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService{
    private final JavaMailSender mailSender;
    private final EmailLogService emailLogService;
    private final TemplateEngine templateEngine;

    @Override
    public void send(EmailEvent event) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true,"UTF-8");

            Context context = new Context();
            context.setVariable("subject", event.subject());
            context.setVariable("name", event.type());
            context.setVariable("message", event.body());

            String htmlContent = templateEngine.process("EmailTemplate.html", context);

            messageHelper.setTo(event.to());
            messageHelper.setSubject(event.subject());
            messageHelper.setText(htmlContent, true);

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
