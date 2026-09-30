package com.example.jobcoach.domain;

import java.time.Instant;

public record MatchSession(
        String id,
        String jobDescription,
        String profile,
        String status,
        MatchReport report,
        Instant createdAt) {
}
