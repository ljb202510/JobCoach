package com.example.jobcoach.web;

import com.example.jobcoach.ai.AiGatewayException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleInvalidBody(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "INPUT_INVALID", "请求字段不符合要求", request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(
            ConstraintViolationException exception,
            HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "INPUT_INVALID", "请求参数不符合要求", request);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableBody(
            HttpMessageNotReadableException exception,
            HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "BODY_INVALID", "请求体不是有效的 JSON", request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleInvalidArgument(
            IllegalArgumentException exception,
            HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST, "INPUT_INVALID", "请求字段不符合要求", request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
            Exception exception,
            HttpServletRequest request) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "服务暂时无法完成请求", request);
    }

    @ExceptionHandler(AiGatewayException.class)
    public ResponseEntity<ApiError> handleAiGateway(
            AiGatewayException exception,
            HttpServletRequest request) {
        HttpStatus status = switch (exception.getCode()) {
            case "AI_CONFIG_INVALID" -> HttpStatus.INTERNAL_SERVER_ERROR;
            case "AI_TIMEOUT" -> HttpStatus.GATEWAY_TIMEOUT;
            default -> HttpStatus.BAD_GATEWAY;
        };
        return error(status, exception.getCode(), "AI 分析服务暂时不可用", request);
    }

    private ResponseEntity<ApiError> error(
            HttpStatus status,
            String code,
            String message,
            HttpServletRequest request) {
        return ResponseEntity.status(status)
                .body(new ApiError(code, message, request.getRequestURI(), Instant.now()));
    }
}
