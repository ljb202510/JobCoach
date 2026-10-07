package com.example.jobcoach.web;

import com.example.jobcoach.ai.AiGatewayException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {
    @Test
    void mapsAiTimeoutToGatewayTimeout() {
        var request = new MockHttpServletRequest("POST", "/api/matches");
        var response = new GlobalExceptionHandler().handleAiGateway(
                new AiGatewayException("AI_TIMEOUT", "AI provider timed out"), request);

        assertEquals(504, response.getStatusCode().value());
        assertEquals("AI_TIMEOUT", response.getBody().code());
        assertEquals("/api/matches", response.getBody().path());
    }
}
