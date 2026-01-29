package com.jovanne.email.dtos;

public record ResendEmailRequestDTO(
                                    String from,
                                    String to,
                                    String subject,
                                    String html) {}
