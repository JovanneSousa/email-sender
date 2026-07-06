package com.jovanne.email.domain.interfaces;

import com.jovanne.email.domain.entities.EmailSendLog;

import java.util.Optional;

public interface IEmailLogRepository {
    Optional<EmailSendLog> getByResendId(String resendId);

    EmailSendLog save(EmailSendLog entity);
}
