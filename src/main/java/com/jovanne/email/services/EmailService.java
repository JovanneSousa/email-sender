package com.jovanne.email.services;

import com.jovanne.email.domain.EmailEvent;
import com.jovanne.email.repositories.IEmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    private final ResendService mailSender;
    private final EmailLogService emailLogService;

    @Override
    public void send(EmailEvent event) {
        try {
//            mailSender.sendEmail(event);
        } catch (Exception ex) {
            emailLogService.logFailure(event, 3, ex);
        }
    }
}
