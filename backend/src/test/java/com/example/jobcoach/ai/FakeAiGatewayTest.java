package com.example.jobcoach.ai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FakeAiGatewayTest {
    private final FakeAiGateway gateway = new FakeAiGateway();

    @Test
    void returnsStableReportFixtureForDemoFlow() {
        var report = gateway.analyze("Java backend", "Java project");

        assertEquals(72, report.matchScore());
        assertEquals(3, report.requirements().size());
        assertEquals("Spring Boot", report.skillGaps().getFirst().skill());
        assertFalse(report.evidence().get(1).matched());
    }
}
