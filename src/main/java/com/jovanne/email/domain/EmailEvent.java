package com.jovanne.email.domain;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record EmailEvent(
        UUID eventId,
        String type,
        String to,
        String locale,
        Map<String, Object> payload,
        Instant createdAt) {}
