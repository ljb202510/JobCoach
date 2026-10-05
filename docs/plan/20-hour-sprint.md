# JobCoach 20 小时冲刺执行手册

本文件是当前冲刺的执行记录与验收表。原五天计划和 30 天路线保留为背景及后续 backlog；本轮总投入按所有 Agent 的有效工作量合计 20 小时。文档、概念学习和 AI coding 记录必须在对应开发阶段同步完成，不能等代码结束后补写。

## 每轮敏捷开发闭环

每个功能迭代都必须按以下顺序执行，并留下对应证据：

1. 需求评审：说明用户问题、价值、非目标和验收样例。
2. 优先级排序：按 P0/P1/P2 决定本轮只扩大一个行为边界。
3. 模块拆分：明确接口、文件所有权、依赖和可回滚的小步。
4. 阅读学习：阅读当天指定的 AI coding、Codex、Java/Spring 或模型概念材料，并写出与当前任务的关系。
5. Plan 与实现：先写实施方案，再小步修改代码。
6. 单元测试：先覆盖成功、输入错误、依赖错误和边界行为。
7. 整体联调：运行 API、前端和必要的真实模型实验。
8. Code Review：逐文件检查契约、异常、日志、隐私、依赖和文档同步。
9. 快速迭代：根据失败证据修复一个最小问题，再运行回归检查。
10. 复盘交付：记录已验证、未验证、取舍、风险和下一轮 backlog。

## 小时块

| 阶段 | 小时 | 产出 | 状态 |
|---|---:|---|---|
| 0. 方案对比与切分 | 1 | 方案记录、接口边界、文件所有权 | 已规划 |
| 1. 真实模型能力探测 | 3 | 脱敏实验记录、结构化解析结果 | 待验证 |
| 2. 后端真实闭环 | 4 | Real gateway、校验、错误映射和测试 | 待验证 |
| 3. 前端联调 | 3 | 成功、加载、错误、重试和报告展示 | 已有 Fake 闭环；真实联调待验证 |
| 4. AI coding 证据整理 | 3 | 会话记录、协作记录、Review 证据 | 进行中 |
| 5. 回归与交付材料 | 4 | README、演示、简历、backlog、最终检查 | 待执行 |

## 分阶段阅读清单

| 阶段 | 必读材料 | 阅读输出 |
|---|---|---|
| 0 | `docs/ai-coding/00-ai-coding-playbook.md`、`03-requirement-decomposition.md`、`04-design-with-ai.md`、`docs/ai-coding/09-multi-agent-collaboration.md` | 需求、优先级、模块边界和 Agent 所有权 |
| 1 | `docs/concepts/02-prompt-and-context.md`、`03-structured-output.md`、`09-model-capabilities.md`、`docs/engineering/model-capability-test-plan.md` | 模型能力假设、实验变量和失败分类 |
| 2 | `docs/concepts/10-ai-application-testing.md`、`12-java-ai-architecture.md`、`docs/ai-coding/05-incremental-implementation.md` | gateway、异常、测试和回退设计 |
| 3 | `docs/ai-coding/07-testing-and-verification.md`、`08-diff-review.md`、`docs/concepts/01-ai-application-overview.md` | 联调检查表、Review 发现和修复记录 |
| 4-5 | `docs/ai-coding/06-debugging-with-ai.md`、`10-personal-methodology.md`、`docs/review/10-retrospective-template.md` | 调试过程、最终取舍、复盘和下一轮 backlog |

## 文件所有权

- Agent A：真实模型适配、AI gateway、后端配置和后端测试。
- Agent B：Vue 页面、API client、前端类型、构建和浏览器联调。
- Agent C：方案对比、实验记录、文档、Review 清单和求职材料。
- 主 Agent：接口契约、集成、冲突处理、最终验证和验收。

跨边界修改先记录接口约定，由主 Agent 集成。密钥只从环境变量读取，日志不包含原始简历、岗位全文或上游 Authorization。

## 验收门禁

- [ ] 真实模型成功返回可解析匹配报告（当前未验证）。
- [x] Fake gateway 可独立回归。
- [ ] 真实或 Fake 模式可完成浏览器演示。
- [ ] 输入错误、模型错误、超时和非法结构有稳定行为。
- [ ] `mvn test -q`、`npm run build`、`git diff --check` 通过。
- [x] 演示、简历、AI coding 记录和下一步 backlog 已有文档入口。

## 暂缓范围

MySQL 持久化、复杂 Tool Calling 副作用、RAG/向量库、登录、文件上传、岗位抓取、模拟面试和产品级多 Agent 不属于本轮硬验收。

## 执行规则

本文件是接下来 20 小时的唯一操作手册。按小时顺序执行；每小时结束后，把目标、阅读材料、动作、文件、命令结果、问题和下一步写入 `docs/ai-coding/20-hour-collaboration-record.md`。没有实际证据的事项保持“待验证”。

每一轮都必须走完：需求评审 -> 优先级排序 -> 模块拆分 -> 阅读学习 -> Plan -> 小步实现 -> 单元测试 -> 整体联调 -> Code Review -> 最小修复 -> 回归 -> 复盘。

## 20 小时逐小时操作

### 1. 上下文和基线

阅读 `PROJECT_GOALS.md`、本文件、`engineering/status-matrix.md`、`ai-coding/01-project-exploration.md`。运行 `git status --short` 和 `rg --files backend frontend docs/plan`，确认 API、Fake gateway、前端入口、测试入口。记录已完成、待验证和暂缓事项。

完成标准：写出本轮唯一目标“真实模型结构化匹配、Fake 回退、可展示联调”。

### 2. 需求评审

阅读 `product/00-product-brief.md`、`product/01-user-flows.md`、`product/02-mvp-scope.md`、`ai-coding/03-requirement-decomposition.md`。写用户故事、成功/失败验收样例和非目标。

完成标准：需求记录包含用户价值、P0 行为、非目标和可验证验收。

### 3. 优先级和任务拆分

P0 只包含真实 gateway、报告校验、错误映射、前端展示和 Fake 回退；P1 记录工具调用边界和实验材料；P2 写入 `next-backlog.md`。拆成后端、前端、测试、文档四个任务，写负责人、依赖和回滚点。

### 4. 方案与接口设计

阅读 `ai-coding/04-design-with-ai.md`、`ai-coding/09-multi-agent-collaboration.md`、`concepts/12-java-ai-architecture.md`。比较 HTTP gateway、LangChain4j、Fake 延期三种方案，固定 `AiGateway`、`MatchReport`、`POST /api/matches` 和错误码契约。

完成标准：方案和接口记录先于代码修改完成。

### 5. Agent 分工和 Codex 流程

Agent A 负责 backend；Agent B 负责 frontend；Agent C 负责实验、Review 和文档；主 Agent 负责契约、集成和验收。每个 Agent 必须先报方案，结束时报告文件、命令、结果和风险。禁止交叉修改所有权文件。

### 6. Prompt 和结构化输出学习

阅读 `concepts/02-prompt-and-context.md`、`concepts/03-structured-output.md`、`concepts/09-model-capabilities.md`。定义 prompt 输入/输出、`MatchReport` 字段和范围，列出完整 JSON、fenced JSON、缺字段、非法 JSON 四类响应。

### 7. 普通模型探测

阅读 `engineering/model-capability-test-plan.md`、`ai-experiments/README.md`。确认 endpoint、模型名和环境变量存在；用脱敏岗位和经历请求一次普通响应，记录状态、响应类别和耗时，不记录密钥或完整用户输入。

### 8. 结构化模型探测

请求固定 `MatchReport` JSON，测试完整 JSON、fenced JSON、缺字段、错误类型和超范围分数。更新 `20-hour-model-experiment.md` 和 `status-matrix.md`，只按实际证据标记成功或待验证。

### 9. 后端 gateway

阅读 `concepts/12-java-ai-architecture.md`、`ai-coding/05-incremental-implementation.md`。保留 Fake，增加 real gateway、环境变量配置、超时、网络失败、配置缺失和上游错误边界。外部协议细节只留在 gateway。

### 10. 后端校验和错误

校验分数和报告数组字段；把配置错误、上游错误、非法输出映射为统一错误码；检查异常消息不包含密钥、简历或岗位全文。运行最快后端测试并记录失败。

### 11. 后端单测

阅读 `concepts/10-ai-application-testing.md`、`engineering/07-testing-strategy.md`。覆盖正常解析、fenced JSON、非法 JSON、缺字段、超范围分数、Fake 回归和 API 输入校验。完成标准：`mvn test -q` 通过。

### 12. 前端状态

阅读 `ai-coding/07-testing-and-verification.md`、`frontend/README.md`。保持 `POST /api/matches`，实现 idle/loading/success/error、20 秒超时、可读错误和最近请求重试。完成标准：`npm run build` 通过。

### 13. 前端报告和联调准备

展示分数、要求、证据、技能差距、风险和建议；检查窄屏、长文本、空状态和错误按钮。用 Fake API 完成一次成功流程，并制造输入错误和后端不可用场景。

### 14. 整体联调

启动 Fake 后端和前端，完成一次浏览器成功分析；再用 real profile 和脱敏输入请求真实模型；确认 API 字段都能展示。分别记录 Fake 和真实模式的实际结果。

### 15. Code Review

阅读 `ai-coding/08-diff-review.md`、`product/07-error-model.md`。逐文件检查需求覆盖、接口一致性、异常、输出校验、日志脱敏、依赖、Fake 回退、文档状态和范围膨胀。记录一个有效取舍、一个 Review 发现和一个风险。

### 16. P0 修复

只选择最高优先级 Review 问题。修改前写原因、最小修复和影响文件；先运行受影响测试，再回归；把修复前后证据写入协作记录。

### 17. 全量回归

依次运行：`cd backend; mvn test -q`、`cd ../frontend; npm run build`、`cd ..; git diff --check`。失败时记录根因和下一步，不修改断言掩盖问题。

### 18. 演示和 README

阅读 `product/08-demo-script.md`、`engineering/02-local-development.md`。从干净终端按 README 启动 Fake 演示，完成成功、错误和重试路径；更新启动命令、环境变量、模式说明和限制。

### 19. 求职表达和最终 Review

阅读 `product/09-resume-description.md`、`review/11-interview-qa.md`、`review/10-retrospective-template.md`。写三条有证据的简历描述，准备 gateway、结构化输出、Fake 回退和错误处理的面试回答，删除无证据的能力宣传。

### 20. 交付和复盘

运行最终门禁；补齐协作记录全部时间块；写已验证、待验证、暂缓和阻塞事项；从 backlog 选下一轮唯一 P0；复盘需求取舍、模块边界、失败实验、Review 修复和学习收获。

## 每小时回报格式

```text
时间块：第 N 小时
目标：
阅读材料与理解：
需求/优先级决定：
修改或实验：
文件：
命令与结果：
Review 发现：
未解决风险：
下一小时动作：
```

## 停止条件

配置缺失、接口冲突、测试失败、敏感信息泄漏、Agent 文件冲突或无法写出验收条件时，暂停扩展范围，先记录证据、修复或明确阻塞，再进入下一个小时。
