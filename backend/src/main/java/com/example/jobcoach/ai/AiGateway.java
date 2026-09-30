package com.example.jobcoach.ai;

public interface AiGateway {
    MatchReport analyze(String jobDescription, String profile);
}
