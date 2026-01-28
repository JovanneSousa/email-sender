package com.jovanne.email.repositories;

import com.jovanne.email.domain.EmailEvent;

public interface IEmailService {
    void send(EmailEvent event);
}
