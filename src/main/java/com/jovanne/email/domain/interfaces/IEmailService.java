package com.jovanne.email.domain.interfaces;

import com.jovanne.email.domain.entities.EmailEvent;

public interface IEmailService {
    void send(EmailEvent event, int attempt);
}
