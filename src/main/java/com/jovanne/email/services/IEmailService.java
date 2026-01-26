package com.jovanne.email.services;

import com.jovanne.email.domain.EmailEvent;

public interface IEmailService {
    void send(EmailEvent event);
}
