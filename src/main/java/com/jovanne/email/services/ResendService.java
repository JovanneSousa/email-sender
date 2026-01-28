package com.jovanne.email.services;

import com.jovanne.email.domain.EmailEvent;
import com.jovanne.email.dtos.ResendEmailRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ResendService {
    private final WebClient client;
    private final String fromDomain;
    private final EmailLogService emailLogService;

    public ResendService(
            @Value("${resend.api.key}") String apiKey,
             @Value("${resend.api.domain}") String fromDomain,
            EmailLogService emailLogService) {
        this.emailLogService = emailLogService;
        this.fromDomain = fromDomain;
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
                                "Email service " + "<" + fromDomain + ">",
                                event.to(),
                                event.subject(),
                                "<p>"+event.body()+"</p>")
                )
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(error ->
                                        {
                                            emailLogService.logFailure(event, "Error Resend: " + error);
                                            return Mono.error(new RuntimeException("Error Resend: " + error));
                                        })
                )
                .bodyToMono(String.class);
    }
}
