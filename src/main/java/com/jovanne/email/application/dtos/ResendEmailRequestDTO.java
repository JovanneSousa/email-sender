package com.jovanne.email.application.dtos;

public record ResendEmailRequestDTO(
                                    String from,
                                    String to,
                                    String subject,
                                    String html) {}
