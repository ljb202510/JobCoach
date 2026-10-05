package com.example.jobcoach.ai;

public class AiGatewayException extends RuntimeException {
    private final String code;

    public AiGatewayException(String code, String message) {
        super(message);
        this.code = code;
    }

    public AiGatewayException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
