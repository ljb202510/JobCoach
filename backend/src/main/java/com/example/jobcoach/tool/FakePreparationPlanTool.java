package com.example.jobcoach.tool;

import com.example.jobcoach.domain.MatchReport;
import com.example.jobcoach.domain.PreparationPlan;
import com.example.jobcoach.domain.PreparationTask;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(prefix = "jobcoach.ai", name = "provider", havingValue = "fake", matchIfMissing = true)
public class FakePreparationPlanTool implements PreparationPlanTool {
    @Override
    public PreparationPlan preview(MatchReport report) {
        List<PreparationTask> tasks = report.skillGaps().stream().limit(3)
                .map(gap -> new PreparationTask(
                        "补充 " + gap.skill() + " 的项目证据",
                        gap.reason(),
                        gap.priority(),
                        "写出一段包含职责、实现和验证结果的项目经历"))
                .toList();
        if (tasks.isEmpty()) {
            tasks = report.recommendations().stream().limit(3)
                    .map(recommendation -> new PreparationTask(
                            "落实匹配建议", recommendation, "MEDIUM", "完成并记录一项可检查的行动"))
                    .toList();
        }
        return new PreparationPlan(null, null, "PREVIEW", tasks);
    }
}
