package com.jovanne.email.domain;

import com.jovanne.email.EmailType;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record EmailEvent(
        String eventId,
        EmailType type,
        String to,
        String subject,
        String body,
        Map<String, Object> metadata
            ) {}
