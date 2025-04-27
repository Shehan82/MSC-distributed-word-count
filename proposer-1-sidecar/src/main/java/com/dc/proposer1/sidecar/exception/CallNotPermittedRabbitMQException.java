package com.dc.proposer1.sidecar.exception;

public class CallNotPermittedRabbitMQException extends RuntimeException {
    public CallNotPermittedRabbitMQException(String message) {
        super(message);
    }
}
