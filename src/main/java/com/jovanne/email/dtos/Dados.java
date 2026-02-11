package com.jovanne.email.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dados {
    String email_id;
    String bounce_type;
    String reason;
}
