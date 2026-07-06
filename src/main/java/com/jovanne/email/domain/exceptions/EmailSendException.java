package com.jovanne.email.domain.exceptions;

public class EmailSendException extends RuntimeException {
    public EmailSendException() {
        super();
    }

    public EmailSendException(String message, Throwable ex) {
        super(message, ex);
    }
}
