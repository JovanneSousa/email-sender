package com.jovanne.email.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Configuration
@Component
@ConfigurationProperties(prefix = "rabbit")
public class RabbitProperties {
    private String host;
    private int port;
    private String user;
    private String pass;

    private String emailQueue;
    private String emailRetryQueue;
    private String emailDlq;

    private String emailExchange;
    private String emailRetryExchange;
    private String emailDlx;

    private String emailRoutingKey;
    private String emailRetryRoutingKey;
    private String emailDlqRoutingKey;
}
