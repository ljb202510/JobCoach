package com.example.jobcoach.tool;

import com.example.jobcoach.ai.AiGatewayException;
import com.example.jobcoach.config.AiProperties;
import com.example.jobcoach.domain.MatchReport;
import com.example.jobcoach.domain.PreparationPlan;
import com.example.jobcoach.domain.PreparationTask;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@ConditionalOnProperty(prefix = "jobcoach.ai", name = "provider", havingValue = "real")
public class RealPreparationPlanTool implements PreparationPlanTool {
    private static final String TOOL_NAME = "preview_preparation_plan";
    private static final Set<String> PRIORITIES = Set.of("HIGH", "MEDIUM", "LOW");
    private final AiProperties properties;
    private final ObjectMapper mapper;
    private final RestClient client;

    public RealPreparationPlanTool(AiProperties properties, ObjectMapper mapper) {
        this.properties = properties;
        this.mapper = mapper;
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        this.client = RestClient.builder().baseUrl(properties.getBaseUrl()).requestFactory(factory).build();
    }

    @Override
    public PreparationPlan preview(MatchReport report) {
        if (properties.getModel() == null || properties.getModel().isBlank()) {
            throw new AiGatewayException("AI_CONFIG_INVALID", "AI model is not configured");
        }
        try {
            String summary = mapper.writeValueAsString(Map.of(
                    "skillGaps", report.skillGaps(), "recommendations", report.recommendations()));
            if (summary.length() > 10_000) {
                throw new IllegalArgumentException("Report is too large for preview");
            }
            var taskSchema = Map.of(
                    "type", "object", "additionalProperties", false,
                    "required", List.of("title", "objective", "priority", "completionCriteria"),
                    "properties", Map.of(
                            "title", Map.of("type", "string"),
                            "objective", Map.of("type", "string"),
                            "priority", Map.of("type", "string", "enum", List.of("HIGH", "MEDIUM", "LOW")),
                            "completionCriteria", Map.of("type", "string")));
            var tool = Map.of("type", "function", "function", Map.of(
                    "name", TOOL_NAME,
                    "description", "Return a read-only preview of preparation tasks; never save or execute actions",
                    "parameters", Map.of("type", "object", "additionalProperties", false,
                            "required", List.of("tasks"), "properties", Map.of("tasks", Map.of(
                                    "type", "array", "minItems", 1, "maxItems", 5, "items", taskSchema)))));
            var request = Map.of(
                    "model", properties.getModel(), "temperature", 0,
                    "messages", List.of(
                            Map.of("role", "system", "content", "根据匹配报告生成1到5个具体准备任务。面向中文用户，任务标题、目标和完成标准除专业术语外均使用中文，不输出英文句子。只调用预览工具，不请求保存、浏览器或外部操作。"),
                            Map.of("role", "user", "content", summary)),
                    "tools", List.of(tool), "tool_choice", Map.of("type", "function", "function", Map.of("name", TOOL_NAME)));
            String body = client.post().uri(uri -> uri.path("/chat/completions").build())
                    .contentType(MediaType.APPLICATION_JSON)
                    .headers(headers -> {
                        if (properties.getApiKey() != null && !properties.getApiKey().isBlank()) {
                            headers.setBearerAuth(properties.getApiKey());
                        }
                    }).body(request).retrieve().body(String.class);
            return parseToolCall(body);
        } catch (AiGatewayException | IllegalArgumentException exception) {
            throw exception;
        } catch (Exception exception) {
            if (isTimeout(exception)) {
                throw new AiGatewayException("AI_TIMEOUT", "AI provider request timed out", exception);
            }
            throw new AiGatewayException("AI_PROVIDER_ERROR", "AI provider request failed", exception);
        }
    }

    PreparationPlan parseToolCall(String body) {
        try {
            if (body == null || body.isBlank()) {
                throw invalid();
            }
            JsonNode root = mapper.readTree(body);
            if (root == null) {
                throw invalid();
            }
            JsonNode calls = root.path("choices").path(0).path("message").path("tool_calls");
            if (!calls.isArray() || calls.size() != 1 || !TOOL_NAME.equals(calls.get(0).path("function").path("name").asText())) {
                throw invalid();
            }
            JsonNode rawArguments = calls.get(0).path("function").path("arguments");
            if (!rawArguments.isTextual()) {
                throw invalid();
            }
            JsonNode arguments = mapper.readTree(rawArguments.asText());
            if (arguments == null || !arguments.isObject() || arguments.size() != 1 || !arguments.path("tasks").isArray()
                    || arguments.path("tasks").isEmpty() || arguments.path("tasks").size() > 5) {
                throw invalid();
            }
            List<PreparationTask> tasks = new ArrayList<>();
            for (JsonNode task : arguments.path("tasks")) {
                if (!task.isObject() || task.size() != 4 || !validChineseText(task, "title")
                        || !validChineseText(task, "objective") || !validChineseText(task, "completionCriteria")
                        || !PRIORITIES.contains(task.path("priority").asText())) {
                    throw invalid();
                }
                tasks.add(new PreparationTask(task.path("title").asText(), task.path("objective").asText(),
                        task.path("priority").asText(), task.path("completionCriteria").asText()));
            }
            return new PreparationPlan(null, null, "PREVIEW", List.copyOf(tasks));
        } catch (AiGatewayException exception) {
            throw exception;
        } catch (JsonProcessingException exception) {
            throw invalid();
        }
    }

    private static boolean validText(JsonNode node, String field) {
        JsonNode value = node.path(field);
        return value.isTextual() && !value.asText().isBlank() && value.asText().length() <= 300;
    }

    private static boolean validChineseText(JsonNode node, String field) {
        return validText(node, field) && node.path(field).asText().codePoints()
                .anyMatch(codePoint -> Character.UnicodeScript.of(codePoint) == Character.UnicodeScript.HAN);
    }

    private static AiGatewayException invalid() {
        return new AiGatewayException("AI_RESPONSE_INVALID", "AI tool call failed validation");
    }

    private static boolean isTimeout(Throwable exception) {
        for (Throwable cause = exception; cause != null; cause = cause.getCause()) {
            if (cause instanceof SocketTimeoutException) {
                return true;
            }
        }
        return false;
    }
}
