# JobCoach 文档导航

所有文档按用途分组。首次进入项目按“目标 → 路线 → 今日任务 → 当前实现”阅读。

## 1. 先读：目标与路线（`plan/`）

- [PROJECT_GOALS.md](plan/PROJECT_GOALS.md)：唯一的目标基准、4:4:2 权重、边界和防偏规则。
- [00-learning-roadmap.md](plan/00-learning-roadmap.md)：五天学习与交付总览。
- [day-1-tasks.md](plan/day-1-tasks.md) 至 [day-5-tasks.md](plan/day-5-tasks.md)：围绕 AI coding 主线推进 JobCoach MVP 的五轮迭代任务、耗时、验收和复盘问题。
- [preflight-assets-plan.md](plan/preflight-assets-plan.md)：用户暂离期间可直接完成的低风险前置工作。
- [01-background-agent-brief.md](plan/01-background-agent-brief.md)：后续 Agent 接手简报。

## 2. 再读：JobCoach 产品与技术设计（`product/`）

- [product/README.md](product/README.md)：已确认需求、方案草案和集中待确认问题。
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
- [requirements-overview.md](product/requirements-overview.md)、[architecture-overview.md](product/architecture-overview.md)、[api-design-overview.md](product/api-design-overview.md)：跨文档概览。

## 3. 实现与验证（`engineering/`）

- [01-environment-setup.md](engineering/01-environment-setup.md)：Java、Node、Docker、MySQL 和模型变量。
- [02-local-development.md](engineering/02-local-development.md)：本机测试、启动、Fake 演示、MySQL 和故障排查。
- [05-ai-capability-matrix.md](engineering/05-ai-capability-matrix.md)：模型渠道能力探测记录。
- [07-testing-strategy.md](engineering/07-testing-strategy.md)：测试范围和证据要求。
- [version-compatibility.md](engineering/version-compatibility.md)：版本核验和官方来源。
- [model-capability-test-plan.md](engineering/model-capability-test-plan.md)：模型能力验证方案。
- [model-capability-results.md](engineering/model-capability-results.md)：真实探测结果（未执行前不填结论）。
- [data-retention-and-privacy.md](engineering/data-retention-and-privacy.md)：输入、报告和日志的保留/隐私草案。
- [dependency-decisions.md](engineering/dependency-decisions.md)：依赖决策记录。
- [multi-agent-task-template.md](engineering/multi-agent-task-template.md)：多 Agent 委派模板。
- [status-matrix.md](engineering/status-matrix.md)：已完成、已确认、待验证和暂缓范围。

## 4. 核心概念（`concepts/`）

- [concepts/README.md](concepts/README.md)：AI 应用、Prompt、结构化输出、Tool、Agent、Workflow、RAG、Memory、兼容性、测试和 Java 架构。

## 前置资产概览

- 当前 `docs/` 共 78 篇 Markdown：`ai-coding` 17 篇、`ai-experiments` 4 篇、`engineering` 12 篇、`plan` 10 篇、`product` 16 篇、`review` 4 篇、`concepts` 13 篇、`archive` 1 篇。
- 核心概念文档：12 篇主题文档，另有 1 份索引。
- AI coding 文档：17 篇流程、方法、模板和会话记录。
- JobCoach 产品文档：12 篇编号规格、3 篇横向概览和 1 份索引。
- 工程准备文档：12 篇，覆盖环境、版本、依赖、模型探测、状态矩阵和多 Agent 模板。
- 每日计划：5 天，按用户确认的 4:4:2 权重编排。

## 5. AI coding 与交付（`ai-coding/`、`review/`）

- [agent-design.md](ai-coding/agent-design.md)：Agent loop、工具和 RAG 边界。
- [codex-workflow.md](ai-coding/codex-workflow.md)：AI coding 全流程、Codex、Skill 和 MCP。
- [11-interview-qa.md](review/11-interview-qa.md)：面试问答初稿。
- [00-preflight-checklist.md](review/00-preflight-checklist.md)：前置资产完成检查表。
- [12-ai-coding-session-record.md](ai-coding/12-ai-coding-session-record.md)：每次 AI coding 任务的证据记录模板。
- [jobcoach-task-template.md](ai-coding/jobcoach-task-template.md)：任务拆解、Codex、Review、调试和测试模板。
- [multi-agent-example.md](ai-coding/multi-agent-example.md)：一次可复用的多 Agent 委派示例。
- [example-session-record.md](ai-coding/example-session-record.md)：本次 Fake 闭环的完整示范记录。

## 6. AI 实验与评测

- [ai-experiments/README.md](ai-experiments/README.md)：无密钥实验入口、夹具、最小评测集和质量清单。

路线补充：[30-day-incremental-roadmap.md](plan/30-day-incremental-roadmap.md)。

## 7. 文档状态与历史（`review/`、`archive/`）

- 当前正式产品：JobCoach。
- [archive-requirement-assistant-goals.md](archive/archive-requirement-assistant-goals.md)：早期未确认方向，仅供历史参考，不得作为需求依据。
