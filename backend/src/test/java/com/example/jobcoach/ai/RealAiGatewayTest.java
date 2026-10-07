package com.example.jobcoach.ai;

import com.example.jobcoach.config.AiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

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

    @Test
    void rejectsEmptyAndNullProviderResponses() {
        for (String response : new String[]{"", "null"}) {
            var error = assertThrows(AiGatewayException.class, () -> gateway().parseResponse(response));
            assertEquals("AI_RESPONSE_INVALID", error.getCode());
        }
    }

    @Test
    void rejectsMissingAndInvalidReportFields() {
        String[] invalidReports = {
                "{\"requirements\":[],\"evidence\":[],\"skillGaps\":[],\"risks\":[],\"recommendations\":[]}",
                "{\"matchScore\":80,\"requirements\":null,\"evidence\":[],\"skillGaps\":[],\"risks\":[],\"recommendations\":[]}",
                "{\"matchScore\":80,\"requirements\":[{\"name\":\"Java\"}],\"evidence\":[],\"skillGaps\":[],\"risks\":[],\"recommendations\":[]}",
                "{\"matchScore\":80,\"requirements\":[],\"evidence\":[{\"requirement\":\"Java\",\"evidence\":\"项目\"}],\"skillGaps\":[],\"risks\":[],\"recommendations\":[]}",
                "{\"matchScore\":80,\"requirements\":[],\"evidence\":[],\"skillGaps\":[],\"risks\":[null],\"recommendations\":[]}"
        };
        for (String report : invalidReports) {
            var error = assertThrows(AiGatewayException.class,
                    () -> gateway().parseResponse(wrap(report)));
            assertEquals("AI_RESPONSE_INVALID", error.getCode());
        }
    }

    @Test
    void rejectsEnglishNarrativeButAllowsTechnicalTerms() {
        var chineseReport = """
                {"matchScore":80,"requirements":[{"name":"Spring Boot","importance":"HIGH"}],
                 "evidence":[{"requirement":"Spring Boot","evidence":"项目经历提到了 Spring Boot","matched":true}],
                 "skillGaps":[{"skill":"MySQL","reason":"缺少索引优化经历","priority":"HIGH"}],
                 "risks":["岗位信息不足"],"recommendations":["补充项目测试证据"]}
                """;
        assertEquals(80, gateway().parseResponse(wrap(chineseReport)).matchScore());

        var englishReport = chineseReport.replace("补充项目测试证据", "Add project testing evidence");
        var error = assertThrows(AiGatewayException.class,
                () -> gateway().parseResponse(wrap(englishReport)));
        assertEquals("AI_RESPONSE_INVALID", error.getCode());
    }

    @Test
    void mapsUpstreamErrorWithoutExposingResponseBody() throws IOException {
        var server = server(503, "upstream private error", 0);
        try {
            var error = assertThrows(AiGatewayException.class,
                    () -> gateway(server, Duration.ofSeconds(1)).analyze("job", "profile"));
            assertEquals("AI_PROVIDER_ERROR", error.getCode());
            assertEquals("AI provider request failed", error.getMessage());
        } finally {
            server.stop(0);
        }
    }

    @Test
    void mapsReadTimeoutSeparately() throws IOException {
        var server = server(200, wrap("{\"matchScore\":80,\"requirements\":[],\"evidence\":[],\"skillGaps\":[],\"risks\":[],\"recommendations\":[]}"), 300);
        try {
            var error = assertThrows(AiGatewayException.class,
                    () -> gateway(server, Duration.ofMillis(50)).analyze("job", "profile"));
            assertEquals("AI_TIMEOUT", error.getCode(), causeTypes(error));
        } finally {
            server.stop(0);
        }
    }

    private RealAiGateway gateway(HttpServer server, Duration readTimeout) {
        var properties = new AiProperties();
        properties.setBaseUrl("http://127.0.0.1:" + server.getAddress().getPort() + "/v1");
        properties.setModel("test-model");
        properties.setReadTimeout(readTimeout);
        return new RealAiGateway(properties, new ObjectMapper());
    }

    private HttpServer server(int status, String body, long delayMillis) throws IOException {
        var server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/v1/chat/completions", exchange -> {
            try (exchange) {
                if (delayMillis > 0) {
                    Thread.sleep(delayMillis);
                }
                byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(status, bytes.length);
                exchange.getResponseBody().write(bytes);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            } catch (IOException ignored) {
                // The client closes the connection after a timeout.
            }
        });
        server.start();
        return server;
    }

    private String wrap(String report) {
        try {
            return "{\"choices\":[{\"message\":{\"content\":"
                    + new ObjectMapper().writeValueAsString(report) + "}}]}";
        } catch (com.fasterxml.jackson.core.JsonProcessingException exception) {
            throw new IllegalArgumentException(exception);
        }
    }

    private String causeTypes(Throwable error) {
        var types = new StringBuilder();
        for (Throwable cause = error; cause != null; cause = cause.getCause()) {
            types.append(cause.getClass().getName()).append(' ');
        }
        return types.toString();
    }
}
