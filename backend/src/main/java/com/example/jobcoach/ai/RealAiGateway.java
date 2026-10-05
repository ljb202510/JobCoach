package com.example.jobcoach.ai;

import com.example.jobcoach.config.AiProperties;
import com.example.jobcoach.domain.MatchReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
@Profile("real")
public class RealAiGateway implements AiGateway {
    private static final String SYSTEM_PROMPT = """
            你是求职匹配分析器。只输出合法 JSON，不要 Markdown 或额外文字。
            JSON 必须包含 matchScore(0到100整数)、requirements(数组，每项含name和importance)、
            evidence(数组，每项含requirement、evidence、matched布尔值)、skillGaps(数组，每项含skill、reason、priority)、
            risks(字符串数组)、recommendations(字符串数组)。根据岗位描述和个人经历给出有证据的分析。
            """;

    private final AiProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient client;

    public RealAiGateway(AiProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        if (properties.getBaseUrl() == null || properties.getBaseUrl().isBlank()) {
            throw new IllegalStateException("AI_BASE_URL is required when real profile is active");
        }
        this.client = RestClient.builder().baseUrl(properties.getBaseUrl()).build();
    }

    @Override
    public MatchReport analyze(String jobDescription, String profile) {
        if (properties.getModel() == null || properties.getModel().isBlank()) {
            throw new AiGatewayException("AI_CONFIG_INVALID", "AI model is not configured");
        }
        try {
            var request = Map.of(
                    "model", properties.getModel(),
                    "temperature", 0,
                    "messages", new Object[]{
                            Map.of("role", "system", "content", SYSTEM_PROMPT),
                            Map.of("role", "user", "content", "岗位描述:\n" + jobDescription + "\n\n个人经历:\n" + profile)
                    });
            String body = client.post()
                    .uri(uri -> uri.path("/chat/completions").build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(headers -> {
                        if (properties.getApiKey() != null && !properties.getApiKey().isBlank()) {
                            headers.setBearerAuth(properties.getApiKey());
                        }
                    })
                    .body(request)
                    .retrieve()
                    .body(String.class);
            return parseResponse(body);
        } catch (AiGatewayException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new AiGatewayException("AI_PROVIDER_ERROR", "AI provider request failed", exception);
        }
    }

    MatchReport parseResponse(String body) {
        try {
            JsonNode root = objectMapper.readTree(body);
            JsonNode content = root.path("choices").path(0).path("message").path("content");
            if (!content.isTextual() || content.asText().isBlank()) {
                throw new AiGatewayException("AI_RESPONSE_INVALID", "AI response has no message content");
            }
            String json = stripMarkdownFence(content.asText().trim());
            MatchReport report = objectMapper.readValue(json, MatchReport.class);
            validate(report);
            return report;
        } catch (AiGatewayException exception) {
            throw exception;
        } catch (JsonProcessingException exception) {
            throw new AiGatewayException("AI_RESPONSE_INVALID", "AI response is not valid match report JSON", exception);
        }
    }

    private static String stripMarkdownFence(String content) {
        if (content.startsWith("```") && content.endsWith("```")) {
            int firstLineEnd = content.indexOf('\n');
            return firstLineEnd >= 0 ? content.substring(firstLineEnd + 1, content.length() - 3).trim() : content;
        }
        return content;
    }

    private static void validate(MatchReport report) {
        if (report == null || report.matchScore() < 0 || report.matchScore() > 100
                || report.requirements() == null || report.evidence() == null
                || report.skillGaps() == null || report.risks() == null || report.recommendations() == null) {
            throw new AiGatewayException("AI_RESPONSE_INVALID", "AI match report failed schema validation");
        }
    }
}
