package com.jovanne.email.controllers;

import com.jovanne.email.dtos.ResendWebhookEvent;
import com.jovanne.email.services.EmailLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks/resend")
@RequiredArgsConstructor
public class ResendWebhookController {

    private final EmailLogService emailLogService;

    @PostMapping
    public ResponseEntity<Void> handleWebhook(@RequestBody ResendWebhookEvent event) {
        var result = emailLogService.updateStatus(event);
        if(!result) return ResponseEntity.badRequest().build();

        return ResponseEntity.ok().build();
    }
}
