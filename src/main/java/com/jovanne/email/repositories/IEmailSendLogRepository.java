package com.jovanne.email.repositories;

import com.jovanne.email.domain.EmailSendLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IEmailSendLogRepository extends JpaRepository<EmailSendLog, UUID> {
}
