package com.jovanne.email.services;

import com.jovanne.email.domain.EmailEvent;
import com.jovanne.email.domain.EmailSendLog;
import com.jovanne.email.dtos.ResendWebhookEvent;
import com.jovanne.email.enums.EmailStatus;
import com.jovanne.email.repositories.IEmailSendLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailLogService {
    private final IEmailSendLogRepository repository;

    //Salva logs sucesso de emails Resend
    public void logSuccess(EmailEvent event, int attempt, String resendId) {
        save(event, EmailStatus.SUCCESS, attempt, null, resendId);
    }

    // Salva log sucesso de emails SMTP
    public void logSuccess(EmailEvent event, int attempt) {
        save(event, EmailStatus.SUCCESS, attempt, null);
    }

    // Salva log falha de emails SMTP
    public void logFailure(EmailEvent event, int attempt, Exception ex) {
        save(event, EmailStatus.FAILED, attempt, ex.getMessage());
    }

    public void markDelivered (ResendWebhookEvent event) {
        updateStatus(EmailStatus.DELIVERED, event);
    }

    public void markFailed (ResendWebhookEvent event) {
        updateStatus(EmailStatus.FAILED, event);
    }

    public void markComplained (ResendWebhookEvent event) {
        updateStatus(EmailStatus.COMPLAINED, event);
    }

    private void updateStatus(EmailStatus status, ResendWebhookEvent event) {
        EmailSendLog logBanco = repository.getByResendId(event.getData().getId());
        if (logBanco == null) {
            log.warn("Email log não encontrado para resendId={}", event.getData().getId());
            return;
        }
        logBanco.setStatus(status);
        if (event.getData().getReason() != null) {
            logBanco.setErrorMessage(event.getData().getReason());
        }
        repository.save(logBanco);
    }

    private void save(EmailEvent event, EmailStatus emailStatus, int attempt, String errorMessage, String resendId) {
        EmailSendLog log = new EmailSendLog(
                event.eventId(),
                event.to(),
                event.subject(),
                emailStatus,
                attempt,
                errorMessage,
                LocalDateTime.now(ZoneId.of("GMT-4")),
                resendId);
        repository.save(log);
    }

    private void save(EmailEvent event, EmailStatus emailStatus, int attempt, String errorMessage) {
        EmailSendLog log = new EmailSendLog(
                event.eventId(),
                event.to(),
                event.subject(),
                emailStatus,
                attempt,
                errorMessage,
                LocalDateTime.now(ZoneId.of("GMT-4")),
                null);
        repository.save(log);
    }
}
