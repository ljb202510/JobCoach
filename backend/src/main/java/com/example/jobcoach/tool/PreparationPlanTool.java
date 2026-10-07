package com.example.jobcoach.tool;

import com.example.jobcoach.domain.PreparationPlan;
import com.example.jobcoach.domain.MatchReport;

public interface PreparationPlanTool {
    PreparationPlan preview(MatchReport report);
}
