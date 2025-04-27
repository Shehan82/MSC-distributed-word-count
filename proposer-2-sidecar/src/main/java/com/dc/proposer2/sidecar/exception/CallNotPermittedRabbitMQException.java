package com.dc.proposer2.sidecar.exception;

public class CallNotPermittedRabbitMQException extends RuntimeException {
    public CallNotPermittedRabbitMQException(String message) {
        super(message);
    }
}
