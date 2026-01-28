package com.jovanne.email.services;

import com.jovanne.email.domain.EmailEvent;
import com.jovanne.email.dtos.ResendEmailRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ResendService {
    private final WebClient client;
    public ResendService(@Value("${resend.api.key}") String apiKey) {
        this.client = WebClient.builder()
                .baseUrl("https://api.resend.com")
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
    }

    public Mono<String> sendEmail(EmailEvent event) {
        return client.post()
                .uri("/emails")
                .bodyValue(
                        new ResendEmailRequestDTO(
                                "${MAIL_USERNAME}",
                                event.to(),
                                event.subject(),
                                event.body())
                )
                .retrieve()
                .bodyToMono(String.class);
    }
}
