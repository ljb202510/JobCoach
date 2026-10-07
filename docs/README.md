# JobCoach 文档导航

所有文档按用途分组。当前执行只以“目标 → MVP 收口计划 → 当前实现”为准；历史计划和日志见 `archive/`，不作为当前目标依据。

## 1. 先读：目标与路线（`plan/`）

- [PROJECT_GOALS.md](plan/PROJECT_GOALS.md)：唯一的目标基准、4:4:2 权重、边界和防偏规则。
- [dev-workflow.md](plan/dev-workflow.md)：可复用开发流程（十个阶段 + 三工具分工 + 并行规则）。
- [mvp-closeout-plan.md](plan/mvp-closeout-plan.md)：当前唯一执行的收口计划。
- [next-backlog.md](plan/next-backlog.md)：当前冲刺之外的候选工作。

## 2. 再读：JobCoach 产品与技术设计（`product/`）

- [product/README.md](product/README.md)：已确认需求、方案草案和集中待确认问题。
- [overview.md](product/overview.md)：跨文档概览（需求/架构/API 合一篇）。
- [00-product-brief.md](product/00-product-brief.md)：产品定位、用户和价值。
- [01-user-flows.md](product/01-user-flows.md)：核心用户流程和失败状态。
- [02-mvp-scope.md](product/02-mvp-scope.md)：P0 范围、暂缓项和取舍规则。
- [03-functional-requirements.md](product/03-functional-requirements.md)：功能需求草案。
- [04-non-functional-requirements.md](product/04-non-functional-requirements.md)：安全、测试、诊断和隐私要求。
- [05-domain-model.md](product/05-domain-model.md)：领域对象草案。
- [06-api-contract.md](product/06-api-contract.md)：当前 API 契约和后续接口。
- [07-error-model.md](product/07-error-model.md)：错误分类和用户行为。
- [08-demo-script.md](product/08-demo-script.md)：演示步骤和 fake 模式说明。
- [09-resume-description.md](product/09-resume-description.md)：简历描述草案。
- [10-technical-highlights.md](product/10-technical-highlights.md)：已验证的技术亮点候选。
- [11-implementation-boundaries.md](product/11-implementation-boundaries.md)：已实现、未实现和局限清单。

## 3. 实现与验证（`engineering/`）

- [01-environment-setup.md](engineering/01-environment-setup.md)：Java、Node、Docker、MySQL 和模型变量。
- [02-local-development.md](engineering/02-local-development.md)：本机测试、启动、Fake 演示、MySQL 和故障排查。
- [07-testing-strategy.md](engineering/07-testing-strategy.md)：测试范围和证据要求。
- [version-compatibility.md](engineering/version-compatibility.md)：版本核验和官方来源。
- [model-capability.md](engineering/model-capability.md)：模型能力矩阵、验证方案与探测结果。
- [data-retention-and-privacy.md](engineering/data-retention-and-privacy.md)：输入、报告和日志的保留/隐私草案。
- [dependency-decisions.md](engineering/dependency-decisions.md)：依赖决策记录。
- [real-model-plan-comparison.md](engineering/real-model-plan-comparison.md)：真实模型接入方案对比（已选 RestClient）。
- [multi-agent-task-template.md](engineering/multi-agent-task-template.md)：多 Agent 委派模板。
- [status-matrix.md](engineering/status-matrix.md)：已完成、已确认、待验证和暂缓范围。

## 4. 核心概念（`concepts/`）

- [concepts/README.md](concepts/README.md)：12 篇主题文档（Prompt、结构化输出、Tool、Agent、Workflow、RAG、Memory、测试、Java 架构等）。

## 5. AI coding 与交付（`ai-coding/`、`review/`）

- [00-ai-coding-playbook.md](ai-coding/00-ai-coding-playbook.md)：AI coding 全流程手册（标准循环 + 职责 + 习惯）。
- [stage-cards.md](ai-coding/stage-cards.md)：十个阶段的详细操作卡。
- [codex-workflow.md](ai-coding/codex-workflow.md)：Codex 的 Workspace/Thread、Plan 与 Skill/MCP 边界。
- [session-record.md](ai-coding/session-record.md)：任务证据记录模板 + Fake 闭环示范。
- [jobcoach-task-template.md](ai-coding/jobcoach-task-template.md)：任务拆解、Codex、Review、调试和测试请求模板。
- [agent-design.md](ai-coding/agent-design.md)：Agent loop、工具和 RAG 边界。
- [multi-agent-example.md](ai-coding/multi-agent-example.md)：一次可复用的多 Agent 委派示例。
- [collaboration-record.md](ai-coding/collaboration-record.md)：当前收口的协作记录（随阶段写入）。
- [concept-evidence.md](ai-coding/concept-evidence.md)：本轮 AI 概念、Skill、MCP 和 Codex 过程证据及未验证边界。
- [00-preflight-checklist.md](review/00-preflight-checklist.md)：前置资产完成检查表。
- [11-interview-qa.md](review/11-interview-qa.md)：面试问答初稿。

## 6. AI 实验与评测

- [ai-experiments/README.md](ai-experiments/README.md)：无密钥实验入口、夹具、最小评测集和质量清单。

## 7. 文档状态与历史（`review/`、`archive/`）

- 当前正式产品：JobCoach。
- [archive/README.md](archive/README.md)：历史计划、研究笔记和旧方向的归档说明。
- [archive-requirement-assistant-goals.md](archive/archive-requirement-assistant-goals.md)：早期未确认方向，仅供历史参考。
