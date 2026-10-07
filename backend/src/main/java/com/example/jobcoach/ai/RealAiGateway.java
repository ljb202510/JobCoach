package com.example.jobcoach.ai;

import com.example.jobcoach.config.AiProperties;
import com.example.jobcoach.domain.MatchReport;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.SocketTimeoutException;
import java.util.Map;

@Component
@ConditionalOnProperty(prefix = "jobcoach.ai", name = "provider", havingValue = "real")
public class RealAiGateway implements AiGateway {
    private static final String SYSTEM_PROMPT = """
            你是求职匹配分析器。只输出合法 JSON，不要 Markdown 或额外文字。
            JSON 必须包含 matchScore(0到100整数)、requirements(数组，每项含name和importance)、
            evidence(数组，每项含requirement、evidence、matched布尔值)、skillGaps(数组，每项含skill、reason、priority)、
            risks(字符串数组)、recommendations(字符串数组)。根据岗位描述和个人经历给出有证据的分析。
            面向中文用户。除 Java、Spring Boot 等专业术语及固定 JSON 字段名外，所有报告说明均使用中文；
            尤其是匹配证据、差距原因、风险和建议，不要输出英文句子。不要将输入中没有的经历或岗位要求写成事实。
            """;

    private final AiProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient client;

    public RealAiGateway(AiProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        if (properties.getBaseUrl() == null || properties.getBaseUrl().isBlank()) {
            throw new IllegalStateException("AI_BASE_URL is required when AI_PROVIDER=real");
        }
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.getConnectTimeout());
        requestFactory.setReadTimeout(properties.getReadTimeout());
        this.client = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .requestFactory(requestFactory)
                .build();
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
            if (isTimeout(exception)) {
                throw new AiGatewayException("AI_TIMEOUT", "AI provider request timed out", exception);
            }
            throw new AiGatewayException("AI_PROVIDER_ERROR", "AI provider request failed", exception);
        }
    }

    MatchReport parseResponse(String body) {
        try {
            if (body == null || body.isBlank()) {
                throw new AiGatewayException("AI_RESPONSE_INVALID", "AI response has no body");
            }
            JsonNode root = objectMapper.readTree(body);
            if (root == null) {
                throw new AiGatewayException("AI_RESPONSE_INVALID", "AI response has no body");
            }
            JsonNode content = root.path("choices").path(0).path("message").path("content");
            if (!content.isTextual() || content.asText().isBlank()) {
                throw new AiGatewayException("AI_RESPONSE_INVALID", "AI response has no message content");
            }
            String json = stripMarkdownFence(content.asText().trim());
            JsonNode result = objectMapper.readTree(json);
            validate(result);
            return objectMapper.treeToValue(result, MatchReport.class);
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

    private static boolean isTimeout(Throwable exception) {
        for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
            if (cause instanceof SocketTimeoutException) {
                return true;
            }
        }
        return false;
    }

    private static void validate(JsonNode report) {
        JsonNode score = report.path("matchScore");
        if (!report.isObject() || !score.isIntegralNumber() || score.longValue() < 0 || score.longValue() > 100
                || !report.path("requirements").isArray() || !report.path("evidence").isArray()
                || !report.path("skillGaps").isArray() || !report.path("risks").isArray()
                || !report.path("recommendations").isArray()) {
            invalidReport();
        }
        for (JsonNode item : report.path("requirements")) {
            if (!item.isObject() || !hasText(item, "name") || !hasLabel(item, "importance")) {
                invalidReport();
            }
        }
        for (JsonNode item : report.path("evidence")) {
            if (!item.isObject() || !hasText(item, "requirement") || !hasText(item, "evidence")
                    || !hasChineseText(item, "evidence") || !item.path("matched").isBoolean()) {
                invalidReport();
            }
        }
        for (JsonNode item : report.path("skillGaps")) {
            if (!item.isObject() || !hasText(item, "skill") || !hasText(item, "reason")
                    || !hasChineseText(item, "reason") || !hasLabel(item, "priority")) {
                invalidReport();
            }
        }
        for (String field : new String[]{"risks", "recommendations"}) {
            for (JsonNode item : report.path(field)) {
                if (!item.isTextual() || item.asText().isBlank() || !containsChinese(item.asText())) {
                    invalidReport();
                }
            }
        }
    }

    private static boolean hasText(JsonNode item, String field) {
        JsonNode value = item.path(field);
        return value.isTextual() && !value.asText().isBlank();
    }

    private static boolean hasChineseText(JsonNode item, String field) {
        return hasText(item, field) && containsChinese(item.path(field).asText());
    }

    private static boolean containsChinese(String text) {
        return text.codePoints().anyMatch(codePoint -> Character.UnicodeScript.of(codePoint) == Character.UnicodeScript.HAN);
    }

    private static boolean hasLabel(JsonNode item, String field) {
        JsonNode value = item.path(field);
        return hasText(item, field) || value.isNumber();
    }

    private static void invalidReport() {
        throw new AiGatewayException("AI_RESPONSE_INVALID", "AI match report failed schema validation");
    }
}
