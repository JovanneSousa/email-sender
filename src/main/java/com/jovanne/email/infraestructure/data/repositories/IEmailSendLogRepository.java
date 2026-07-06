package com.jovanne.email.infraestructure.data.repositories;

import com.jovanne.email.domain.entities.EmailSendLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IEmailSendLogRepository extends JpaRepository<EmailSendLog, UUID> {
    Optional<EmailSendLog> getByResendId(String resendId);
}
