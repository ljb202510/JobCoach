package com.example.jobcoach.application;

import com.example.jobcoach.ai.AiGateway;
import com.example.jobcoach.domain.MatchReport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class MatchAnalysisServiceTest {
    @Test
    void passesUserInputsToGatewayAndReturnsItsReport() {
        var expected = new MatchReport(10, List.of(), List.of(), List.of(), List.of(), List.of());
        var gateway = new RecordingGateway(expected);
        var service = new MatchAnalysisService(gateway);

        assertSame(expected, service.analyze("job text", "profile text"));
        assertEquals("job text", gateway.jobDescription);
        assertEquals("profile text", gateway.profile);
    }

    private static final class RecordingGateway implements AiGateway {
        private final MatchReport result;
        private String jobDescription;
        private String profile;

        private RecordingGateway(MatchReport result) {
            this.result = result;
        }

        @Override
        public MatchReport analyze(String jobDescription, String profile) {
            this.jobDescription = jobDescription;
            this.profile = profile;
            return result;
        }
    }
}
