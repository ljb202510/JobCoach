package com.example.jobcoach.application;

import com.example.jobcoach.ai.AiGateway;
import com.example.jobcoach.domain.MatchReport;
import org.springframework.stereotype.Service;

@Service
public class MatchAnalysisService {
    private final AiGateway aiGateway;

    public MatchAnalysisService(AiGateway aiGateway) {
        this.aiGateway = aiGateway;
    }

    public MatchReport analyze(String jobDescription, String profile) {
        return aiGateway.analyze(jobDescription, profile);
    }
}
