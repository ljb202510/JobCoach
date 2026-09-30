package com.example.jobcoach.ai;

import com.example.jobcoach.domain.MatchReport;

public interface AiGateway {
    MatchReport analyze(String jobDescription, String profile);
}
