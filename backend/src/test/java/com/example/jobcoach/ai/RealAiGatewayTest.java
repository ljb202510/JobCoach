package com.example.jobcoach.ai;

import com.example.jobcoach.config.AiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RealAiGatewayTest {
    private RealAiGateway gateway() {
        var properties = new AiProperties();
        properties.setBaseUrl("http://localhost:9999");
        properties.setModel("test-model");
        return new RealAiGateway(properties, new ObjectMapper());
    }

    @Test
    void parsesOpenAiMessageContentAndMarkdownFence() {
        var response = """
                {"choices":[{"message":{"content":"```json\\n{\\\"matchScore\\\":88,\\\"requirements\\\":[],\\\"evidence\\\":[],\\\"skillGaps\\\":[],\\\"risks\\\":[],\\\"recommendations\\\":[]}\\n```"}}]}
                """;
        var report = gateway().parseResponse(response);
        assertEquals(88, report.matchScore());
    }

    @Test
    void rejectsOutOfRangeScore() {
        var response = "{\"choices\":[{\"message\":{\"content\":\"{\\\"matchScore\\\":101,\\\"requirements\\\":[],\\\"evidence\\\":[],\\\"skillGaps\\\":[],\\\"risks\\\":[],\\\"recommendations\\\":[]}\"}}]}";
        var error = assertThrows(AiGatewayException.class, () -> gateway().parseResponse(response));
        assertEquals("AI_RESPONSE_INVALID", error.getCode());
    }
}
