# 第 3 天：真实 AI 能力和受控工具的敏捷迭代

预计 6-8 小时。本天不是单独做“模型实验”，而是从 Day 2 的真实 backlog 中选择一个能力增量，把真实模型、结构化输出和受控工具接入现有 MVP，并保留可回退的 Fake 路径。

## 本天迭代目标

让匹配报告具备真实模型适配边界，同时为后续准备任务动作定义确认和校验规则；本天不引入数据库，也不执行持久化副作用。

## 阅读与学习清单（约 90 分钟）

### 必读项目文档

- `docs/concepts/03-structured-output.md`：学习模型响应 DTO、Schema 校验和领域校验的三层区别。
- `docs/concepts/04-tool-calling.md`：学习工具描述、参数校验、权限、幂等、超时和审计。
- `docs/concepts/05-agent-loop.md`：学习观察、决策、工具、结果、停止条件和预算。
- `docs/concepts/06-workflow-vs-agent.md`：学习什么时候用固定 Workflow，什么时候才需要 Agent。
- `docs/concepts/09-model-capabilities.md`：学习 OpenAI 格式兼容不等于能力完整兼容。
- `docs/engineering/model-capability-test-plan.md`：学习实验输入、脱敏、记录字段和失败分流。
- `docs/ai-coding/04-design-with-ai.md`、`06-debugging-with-ai.md`：学习高风险方案评审和基于假设的调试。

### 必读代码与测试

- `backend/src/main/java/com/example/jobcoach/application/MatchAnalysisService.java`：学习用例层如何调用 AI gateway。
- `backend/src/main/java/com/example/jobcoach/tool/PreparationPlanTool.java`：学习副作用端口和用户确认边界。
- `backend/src/main/java/com/example/jobcoach/persistence/ExecutionRecorder.java`：学习执行步骤如何留下诊断证据。
- `backend/src/test/java/com/example/jobcoach/ai/FakeAiGatewayTest.java`：学习如何先验证稳定替身，再替换真实依赖。

### 今天必须掌握的概念

结构化输出、Tool Calling、Workflow、Agent loop、Skill 和 MCP。重点回答：模型可以提出什么，应用必须重新校验什么，MCP 解决哪一层连接问题，为什么保存任务不能由模型直接决定。

### 阅读后的输出

完成一张“模型能力与降级路径”表：每项能力的成功条件、失败条件、Fake/Java 侧替代方案和是否进入 MVP。再写出 `savePreparationPlan` 的最小权限和幂等规则。

## 固定 AI coding 循环

```text
评审 Fake 主链路 -> 选择一个 AI 能力增量 -> 能力探测
-> adapter/Schema/工具实现 -> 失败降级
-> 单测/实验/权限测试 -> Review -> 回归 -> 更新 backlog
```

## 步骤

### 1. 选择本轮能力（45 分钟）

1. 阅读 Day 2 的 backlog 和能力矩阵。
2. 只选择一条主增量：真实文本/结构化报告，或受控准备任务工具；其他能力进入候选。
3. 检查环境变量、provider、模型和密钥来源，确认实验输入已脱敏。
4. 写成功、失败、超时、非法响应和降级验收。

### 2. 先做最小探测（60 分钟）

1. 用不含隐私的固定输入验证普通文本请求。
2. 再验证结构化响应是否真的满足字段、类型、枚举和范围。
3. 记录 provider、模型、状态码、耗时、结果类别和替代路径。
4. 不把“OpenAI 格式兼容”写成 Tool Calling、流式或 Embedding 已支持。

### 3. 接入真实模型适配器（90 分钟）

1. 保持 `AiGateway` 领域接口不变。
2. 在 adapter 中处理 provider 请求、超时、原始响应和异常转换。
3. 将模型响应转换为内部 DTO，再经过领域校验后才进入 Application。
4. 非法 JSON、缺字段、额外文本、无证据断言都要有受控失败行为。
5. 配置关闭真实模型时，Fake gateway 仍能运行全部业务测试。

### 4. 设计受控工具（90 分钟）

1. 从报告生成准备任务预览，要求用户显式确认；当前只保留预览或 Fake 记录。
2. 定义 `savePreparationPlan` 的参数、报告关联、任务数量、幂等键和状态。
3. 在应用层校验工具名称、权限、资源归属和重复请求。
4. 记录 requested、validated、rejected 等状态；真正 executed 和持久化留到后续路线。
5. 用 Fake repository 或无副作用 adapter 完成测试，不直接连接真实数据库。

### 5. 比较 Workflow、Agent、Skill 和 MCP（60 分钟）

1. 将“分析 → 报告 → 用户确认 → 保存”保留为固定 Workflow。
2. 设计一个受限 Agent 场景：报告缺技能证据时选择一个白名单检索工具。
3. 将重复的能力探测/Review 流程写成项目 Skill 草案，包含 `SKILL.md`、触发描述和验收。
4. 说明 MCP 是连接外部能力的协议边界，不能替代权限校验和业务流程。
5. 对每个概念记录它解决的当前问题和暂不使用的理由。

### 6. 测试、Review 和回退（75 分钟）

1. 运行 Fake 回归测试、真实模型实验、结构校验测试和工具权限测试。
2. 模拟模型超时、provider 失败、非法结构、未知工具、越权参数和重复保存。
3. 让 Codex Review adapter、Prompt、工具授权、日志和降级行为。
4. 人工确认真实实验没有密钥和个人隐私，再更新能力矩阵和状态矩阵。

## 本天交付

- 真实模型能力探测记录
- 真实 adapter 或明确的失败/降级记录
- 结构化输出解析和领域校验
- 受控准备任务工具的设计、确认边界和无副作用测试记录
- Skill/MCP/Agent/Workflow 的实际使用记录
- Day 4 前端联调 backlog

## 本天验收

- 能明确哪些能力是真实测试结果，哪些仍未验证。
- 真实模型失败时 Fake 主链路仍可演示。
- 工具不能越权、重复产生副作用或绕过用户确认。
- `mvn test -q` 通过，实验结果可脱敏复查。

## 复盘

1. 真实模型增加了什么用户价值，又增加了什么失败风险？
2. 哪一步使用 Agent 是必要的，哪一步用 Workflow 更合理？
3. 这个过程中 Skill 是否真的复用了流程，还是只是换了一个 Prompt 名称？
