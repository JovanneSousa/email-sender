package com.jovanne.email.application.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendWebhookEvent {
    String type;
    Dados data;
}
