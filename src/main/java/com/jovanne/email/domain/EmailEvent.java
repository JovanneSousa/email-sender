package com.jovanne.email.domain;

import com.jovanne.email.enums.EmailType;
import java.util.Map;

public record EmailEvent(
        String eventId,
        EmailType type,
        String to,
        String subject,
        String body,
        Map<String, Object> metadata
            ) {}
