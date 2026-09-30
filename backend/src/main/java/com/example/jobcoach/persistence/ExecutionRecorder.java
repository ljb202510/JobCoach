package com.example.jobcoach.persistence;

public interface ExecutionRecorder {
    void record(String executionId, String stepType, String status, String safeSummary);
}
