package com.example.jobcoach.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MatchControllerTest {

    @Test
    void returnsStructuredMatchReport() throws Exception {
        var report = new com.example.jobcoach.ai.FakeAiGateway()
                .analyze("Java backend", "Java project");
        org.junit.jupiter.api.Assertions.assertEquals(72, report.matchScore());
        org.junit.jupiter.api.Assertions.assertEquals("Spring Boot", report.skillGaps().getFirst().skill());
    }

    @Test
    void rejectsBlankJobDescription() throws Exception {
        var request = new MatchController.MatchRequest("", "Java project");
        var violations = jakarta.validation.Validation.buildDefaultValidatorFactory()
                .getValidator().validate(request);
        org.junit.jupiter.api.Assertions.assertFalse(violations.isEmpty());
    }
}
