package com.jovanne.email.infraestructure.data.repositories;

import com.jovanne.email.domain.entities.EmailSendLog;
import com.jovanne.email.domain.interfaces.IEmailLogRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaEmailLogRepository implements IEmailLogRepository {
    private final IEmailSendLogRepository repository;

    public JpaEmailLogRepository(IEmailSendLogRepository repository) {
        this.repository = repository;
    }
    @Override
    public Optional<EmailSendLog> getByResendId(String resendId) {
        return repository.getByResendId(resendId);
    }

    @Override
    public EmailSendLog save(EmailSendLog entity) {
        return repository.save(entity);
    }
}
