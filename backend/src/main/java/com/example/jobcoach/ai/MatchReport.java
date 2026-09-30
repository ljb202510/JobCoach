package com.example.jobcoach.domain;

import java.util.List;

public record MatchReport(
        int matchScore,
        List<JobRequirement> requirements,
        List<MatchEvidence> evidence,
        List<SkillGap> skillGaps,
        List<String> risks,
        List<String> recommendations) {

    public record JobRequirement(String name, String importance) {
    }

    public record MatchEvidence(String requirement, String evidence, boolean matched) {
    }

    public record SkillGap(String skill, String reason, String priority) {
    }
}
