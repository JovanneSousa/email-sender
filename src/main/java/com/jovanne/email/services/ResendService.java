package com.jovanne.email.services;

import com.jovanne.email.domain.EmailEvent;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
public class ResendService implements IEmailService {
    private final String fromDomain;
    private final Resend resend;
    private final EmailLogService emailLogService;
    private final EmailTemplateService emailTemplateService;

    public ResendService(
            @Value("${resend.api.key}") String apiKey,
             @Value("${resend.api.domain}") String fromDomain,
            EmailLogService emailLogService,
            EmailTemplateService emailTemplateService) {
        this.resend = new Resend(apiKey);
        this.fromDomain = "Email service " + "<" + fromDomain + ">";
        this.emailLogService = emailLogService;
        this.emailTemplateService = emailTemplateService;
    }

    public void send(EmailEvent event, int attempt) {

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromDomain)
                .to(event.to())
                .subject(event.subject())
                .html(emailTemplateService.renderEmail(event))
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            emailLogService.logSuccess(event, attempt, data.getId());
            log.info(
                    "Email enviado com sucesso | eventId={} | to={} | resendId={}",
                    event.eventId(), event.to(), data.getId()
            );
        } catch (ResendException e) {
            emailLogService.logFailure(event, attempt, e);
            log.error(
                    "Falha ao enviar email | eventId={} | to={}",
                    event.eventId(), event.to()
            );
        }
    }
}
