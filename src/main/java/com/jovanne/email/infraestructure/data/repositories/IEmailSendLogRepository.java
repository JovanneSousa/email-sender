package com.jovanne.email.infraestructure.data.repositories;

import com.jovanne.email.domain.entities.EmailSendLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IEmailSendLogRepository extends JpaRepository<EmailSendLog, UUID> {
    public EmailSendLog getByResendId(String resendId);
}
