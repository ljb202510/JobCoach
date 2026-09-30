package com.example.jobcoach.ai;

import com.example.jobcoach.domain.MatchReport;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("fake")
public class FakeAiGateway implements AiGateway {
    @Override
    public MatchReport analyze(String jobDescription, String profile) {
        return new MatchReport(
                72,
                List.of(
                        new MatchReport.JobRequirement("Java", "HIGH"),
                        new MatchReport.JobRequirement("Spring Boot", "HIGH"),
                        new MatchReport.JobRequirement("AI 应用开发", "MEDIUM")),
                List.of(
                        new MatchReport.MatchEvidence("Java", "个人经历中包含 Java 后端项目", true),
                        new MatchReport.MatchEvidence("Spring Boot", "经历文本未提供足够证据", false)),
                List.of(new MatchReport.SkillGap("Spring Boot", "缺少可验证的项目细节", "HIGH")),
                List.of("当前报告由 fake gateway 生成，仅用于业务链路测试"),
                List.of("补充 Spring Boot 项目的职责、接口和测试证据"));
    }
}
