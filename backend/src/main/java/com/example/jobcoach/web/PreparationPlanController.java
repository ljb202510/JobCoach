package com.example.jobcoach.web;

import com.example.jobcoach.domain.MatchReport;
import com.example.jobcoach.domain.PreparationPlan;
import com.example.jobcoach.tool.PreparationPlanTool;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/preparation-plans")
public class PreparationPlanController {
    private final PreparationPlanTool tool;

    public PreparationPlanController(PreparationPlanTool tool) {
        this.tool = tool;
    }

    @PostMapping("/preview")
    public ResponseEntity<PreparationPlan> preview(@RequestBody MatchReport report) {
        if (report == null || report.skillGaps() == null || report.recommendations() == null
                || (report.skillGaps().isEmpty() && report.recommendations().isEmpty())
                || report.skillGaps().size() > 10 || report.recommendations().size() > 10
                || report.toString().length() > 10_000
                || report.skillGaps().stream().anyMatch(gap -> gap == null || blank(gap.skill())
                        || blank(gap.reason()) || blank(gap.priority()))
                || report.recommendations().stream().anyMatch(PreparationPlanController::blank)) {
            throw new IllegalArgumentException("Invalid report for preparation preview");
        }
        return ResponseEntity.ok(tool.preview(report));
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank() || value.length() > 300;
    }
}
