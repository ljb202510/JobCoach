# 第 1 天：需求评审到第一个垂直切片

预计 6-8 小时。本天交付的不是一份孤立的产品文档，而是从真实需求开始，完成第一轮 AI coding，并留下一个能运行的最小垂直切片。

## 本天迭代目标

把“我想做一个 AI 求职教练”收敛成一个可开发任务：用户提交岗位描述和个人经历，系统返回一个可验证的匹配结果。先使用现有 Fake 能力或最小固定响应，重点练习需求评审、优先级、任务拆分和验收。

## 阅读与学习清单（约 90 分钟）

### 必读项目文档

- `docs/plan/PROJECT_GOALS.md`：学习项目的最终目标、4:4:2 权重、P0/P1/P2 和“事实/草案/待验证”的区别。
- `docs/product/00-product-brief.md`：学习 JobCoach 服务的用户、问题和不承诺内容。
- `docs/product/01-user-flows.md`：学习正常流程、失败状态和用户确认点。
- `docs/product/02-mvp-scope.md`：学习如何把功能分成 P0、P1、P2，并写出取舍理由。
- `docs/engineering/status-matrix.md`：学习如何区分已经验证的代码事实和计划中的能力。

### 必读 AI coding 文档

- `docs/ai-coding/01-project-exploration.md`：学习如何从目录、入口、测试和 Git 状态建立项目地图。
- `docs/ai-coding/02-context-preparation.md`：学习如何准备目标、约束、非目标和验收上下文。
- `docs/ai-coding/03-requirement-decomposition.md`：学习如何把一句需求拆成用户故事、状态、模块和小任务。
- `docs/ai-coding/codex-workflow.md`：学习 Workspace、Thread、Plan 和小步执行的使用边界。

### 今天必须掌握的概念

Prompt、Context、Workflow、Agent、Tool、RAG、Skill、MCP。重点不是背定义，而是回答：它们分别解决什么问题，今天为什么只使用部分概念，哪些能力暂时不应该加入 MVP。

### 阅读后的输出

写一页“JobCoach 第一轮开发说明”：用户故事、P0 验收、失败状态、非目标、受影响文件和 Codex 任务提示词。没有这份输出，不进入 Plan 和编码。

## 固定 AI coding 循环

```text
需求评审 -> P0/P1/P2 排序 -> 模块拆分 -> Codex Plan
-> 修改最小代码/契约 -> 单元或接口测试 -> Review
-> 手工运行 -> 记录问题 -> 形成 Day 2 backlog
```

## 步骤

### 1. 读取现状并建立上下文（45 分钟）

1. 阅读 `PROJECT_GOALS.md`、产品简述、用户流程和当前状态矩阵。
2. 运行 `git status --short`、`rg --files backend frontend docs`、`mvn test -q`、`npm run build`。
3. 阅读当前 `MatchController`、`MatchRequest`、`MatchReport`、`AiGateway` 和前端 API client。
4. 将结论分成事实、用户确认、草案、待验证；把已有未提交改动记录到任务记录。

产出：当前行为说明、基线测试结果、相关文件清单。

### 2. 评审需求并排优先级（60 分钟）

1. 写出用户故事：“作为求职者，我希望输入岗位和经历，看到有证据的匹配差距和下一步准备任务。”
2. 列正常路径：输入 → 分析 → 报告 → 准备任务；列失败路径：空输入、超长、模型失败、输出非法、保存失败。
3. 将需求排序：
   - P0：输入、匹配报告、证据/差距、错误状态、Fake 演示。
   - P1：真实模型、结构化输出、准备计划保存、执行记录。
   - P2：RAG、登录、文件上传、多 Agent、生产部署。
4. 对每个 P0 写可观察验收，例如状态码、响应字段、页面状态或测试断言。
5. 明确本轮非目标，防止 Codex 在实现时顺手扩展。

产出：用户故事、优先级表、验收清单、非目标清单。

### 3. 用 Codex 做方案和任务拆分（60 分钟）

1. 在当前工作区新建一个只负责“匹配主链路”的 Thread。
2. 让 Codex 先只读探索，要求输出事实、未知、受影响文件和风险。
3. 进入 Plan，要求拆成契约、应用层、Fake gateway、测试和最小前端适配五个小任务。
4. 审查方案是否引入了未授权的模型、数据库、工具或架构重构。
5. 将选定方案写入会话记录，并为每个小任务指定最快验证命令。

产出：Plan、任务列表、文件所有权、风险和回滚点。

### 4. 实现第一个垂直切片（120 分钟）

1. 先实现或确认请求 DTO、错误模型和响应契约。
2. 让 Controller 调用 Application service，而不是在 Controller 中生成业务结果。
3. 使用 Fake gateway 返回稳定的匹配报告，保证报告包含要求、证据、差距和建议。
4. 让前端或 HTTP 客户端能提交一组样例并看到响应。
5. 每完成一个小任务就运行最快相关测试，不积累到最后一次性检查。

### 5. 测试、Review 和手工验证（90 分钟）

1. 补正常输入、空输入、超长输入和 gateway 异常测试。
2. 运行 `mvn test -q`、`npm run build`、`git diff --check`。
3. 让 Codex Review：需求遗漏、错误路径、敏感信息、无关修改和测试缺口。
4. 人工复核 Review 结论，修复高优先级问题，再运行相关测试。
5. 用 HTTP 或页面手工走通一次 P0 成功路径。

### 6. 形成下一轮 backlog（30 分钟）

记录本轮完成、未完成、失败尝试、实际证据和 Day 2 优先任务。Day 2 的任务必须来自今天发现的真实缺口，不能重新凭空设计。

## 本天交付

- P0/P1/P2 优先级和验收标准
- 一个独立 Thread 的 Codex Plan 和会话记录
- 可调用的 Fake 匹配垂直切片
- 测试、Review、手工验证证据
- Day 2 backlog

## 本天验收

- 能解释需求为什么这样排序。
- 能从用户动作追到代码入口、测试和响应。
- 至少有一个真实代码行为经过“方案 → 实现 → 测试 → Review → 手工验证”。
- `mvn test -q` 和 `npm run build` 通过。

## 复盘

1. Codex 哪个方案建议被你拒绝了，为什么？
2. 哪个 P1/P2 需求如果提前加入会破坏本轮节奏？
3. 下一轮 backlog 是由什么测试或手工验证发现的？
