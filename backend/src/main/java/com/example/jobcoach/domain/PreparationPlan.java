package com.example.jobcoach.domain;

import java.util.List;

public record PreparationPlan(
        String id,
        String matchId,
        String status,
        List<PreparationTask> tasks) {
}
