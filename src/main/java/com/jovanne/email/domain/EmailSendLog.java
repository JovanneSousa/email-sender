package com.jovanne.email.domain;

import com.jovanne.email.enums.EmailStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_email_log")
@Getter
@Setter
@NoArgsConstructor
public class EmailSendLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String eventId;
    private String toEmail;
    private String subject;

    @Enumerated(EnumType.STRING)
    private EmailStatus status;

    private Integer attempt;

    private String errorMessage;

    private LocalDateTime createdAt;

    public String resendId;

    public  EmailSendLog(
            String event,
            String to,
            String corpo,
            EmailStatus emailStatus,
            Integer tentativa,
            String error,
            LocalDateTime dataCriacao,
            String resendId) {
        eventId = event;
        toEmail = to;
        subject = corpo;
        status = emailStatus;
        attempt = tentativa;
        errorMessage = error;
        createdAt = dataCriacao;
        this.resendId = resendId;
    }

}
