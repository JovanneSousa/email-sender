package com.jovanne.email.services;

import com.jovanne.email.domain.EmailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {
    private final TemplateEngine templateEngine;

    public String renderEmail(EmailEvent event) {
        Context context = new Context();
        context.setVariable("subject", event.subject());
        context.setVariable("name", event.type());
        context.setVariable("message", event.body());

        return templateEngine.process("EmailTemplate.html", context);
    }
}
