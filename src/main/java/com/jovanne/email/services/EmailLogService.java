package com.jovanne.email.services;

import com.jovanne.email.domain.EmailEvent;
import com.jovanne.email.domain.EmailSendLog;
import com.jovanne.email.enums.EmailStatus;
import com.jovanne.email.repositories.IEmailSendLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailLogService {
    private final IEmailSendLogRepository repository;

    public void logSuccess(EmailEvent event, int attempt) {
        save(event, EmailStatus.SUCCESS, attempt, null);
    }

    public void logFailure(EmailEvent event, int attempt, Exception ex) {
        save(event, EmailStatus.FAILED, attempt, ex.getMessage());
    }

    public void logFailure(EmailEvent event, String error) {
        save(event, EmailStatus.FAILED, 1, error);
    }

    private void save(EmailEvent event, EmailStatus emailStatus, int attempt, String errorMessage) {
        EmailSendLog log = new EmailSendLog(
                event.eventId(),
                event.to(),
                event.subject(),
                emailStatus,
                attempt,
                errorMessage,
                LocalDateTime.now());
        repository.save(log);
    }

}
