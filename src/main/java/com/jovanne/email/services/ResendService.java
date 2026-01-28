package com.jovanne.email.services;

import com.jovanne.email.domain.EmailEvent;
import com.jovanne.email.dtos.ResendEmailRequestDTO;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ResendService {
    private final String fromDomain;
    private final Resend resend;
    private final EmailLogService emailLogService;
    private final TemplateEngine templateEngine;

    public ResendService(
            @Value("${resend.api.key}") String apiKey,
             @Value("${resend.api.domain}") String fromDomain,
            EmailLogService emailLogService,
            TemplateEngine templateEngine) {
        this.resend = new Resend(apiKey);
        this.fromDomain = "Email service " + "<" + fromDomain + ">";
        this.emailLogService = emailLogService;
        this.templateEngine = templateEngine;
    }

    public void sendEmail(EmailEvent event, int attempt) {
        Context context = new Context();
        context.setVariable("subject", event.subject());
        context.setVariable("name", event.type());
        context.setVariable("message", event.body());

        String htmlContent =
                templateEngine.process("EmailTemplate.html", context);

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from(fromDomain)
                .to(event.to())
                .subject(event.subject())
                .html(htmlContent)
                .build();

        try {
            CreateEmailResponse data = resend.emails().send(params);
            emailLogService.logSuccess(event, attempt);
            log.info(
                    "Email enviado com sucesso | eventId={} | to={}",
                    event.eventId(), event.to()
            );
            System.out.println(data.toString());
        } catch (ResendException e) {
            emailLogService.logFailure(event, attempt, e);
            log.error(
                    "Falha ao enviar email | eventId={} | to={}",
                    event.eventId(), event.to()
            );
        }
    }
}
