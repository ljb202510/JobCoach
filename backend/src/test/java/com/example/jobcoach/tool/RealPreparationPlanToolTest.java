package com.example.jobcoach.tool;

import com.example.jobcoach.ai.AiGatewayException;
import com.example.jobcoach.config.AiProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RealPreparationPlanToolTest {
    private final ObjectMapper mapper = new ObjectMapper();
    private final RealPreparationPlanTool tool = tool();

    @Test
    void acceptsOnlyPreviewTasksWithoutIds() throws Exception {
        var plan = tool.parseToolCall(response("preview_preparation_plan", Map.of("tasks", new Object[]{task()})));
        assertEquals("PREVIEW", plan.status());
        assertNull(plan.id());
        assertNull(plan.matchId());
        assertEquals(1, plan.tasks().size());
    }

    @Test
    void rejectsUnknownToolAndExtraSideEffectArguments() throws Exception {
        assertInvalid(response("save_preparation_plan", Map.of("tasks", new Object[]{task()})));
        assertInvalid(response("preview_preparation_plan", Map.of(
                "tasks", new Object[]{task()}, "save", true)));
        var actionTask = new HashMap<>(task());
        actionTask.put("externalAction", "send email");
        assertInvalid(response("preview_preparation_plan", Map.of("tasks", new Object[]{actionTask})));
    }

    @Test
    void rejectsMalformedToolCallAndInvalidTask() throws Exception {
        assertInvalid("{}");
        assertInvalid("null");
        assertInvalid(response("preview_preparation_plan", Map.of("tasks", new Object[]{})));
        var invalidPriority = new HashMap<>(task());
        invalidPriority.put("priority", "URGENT");
        assertInvalid(response("preview_preparation_plan", Map.of("tasks", new Object[]{invalidPriority})));
    }

    @Test
    void rejectsEnglishTaskDescription() throws Exception {
        var englishTask = new HashMap<>(task());
        englishTask.put("objective", "Build an API");
        assertInvalid(response("preview_preparation_plan", Map.of("tasks", new Object[]{englishTask})));
    }

    private RealPreparationPlanTool tool() {
        var properties = new AiProperties();
        properties.setBaseUrl("http://127.0.0.1:9/v1");
        properties.setModel("test-model");
        return new RealPreparationPlanTool(properties, mapper);
    }

    private Map<String, String> task() {
        return Map.of("title", "练习 Spring Boot", "objective", "实现一个 API",
                "priority", "HIGH", "completionCriteria", "补充一项集成测试");
    }

    private String response(String name, Object arguments) throws Exception {
        return mapper.writeValueAsString(Map.of("choices", new Object[]{Map.of("message", Map.of(
                "tool_calls", new Object[]{Map.of("function", Map.of(
                        "name", name, "arguments", mapper.writeValueAsString(arguments)))}) )}));
    }

    private void assertInvalid(String response) {
        var error = assertThrows(AiGatewayException.class, () -> tool.parseToolCall(response));
        assertEquals("AI_RESPONSE_INVALID", error.getCode());
    }
}
