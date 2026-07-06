package com.jovanne.email.domain.entities;

import com.jovanne.email.domain.enums.EmailType;
import java.util.Map;

public record EmailEvent(
        String eventId,
        EmailType type,
        String to,
        String subject,
        String body,
        Map<String, Object> metadata
            ) {}
