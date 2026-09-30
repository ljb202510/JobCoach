package com.example.jobcoach.tool;

import com.example.jobcoach.domain.PreparationPlan;

/** Port for the explicitly approved preparation-plan side effect. */
public interface PreparationPlanTool {
    PreparationPlan save(PreparationPlan plan);
}
