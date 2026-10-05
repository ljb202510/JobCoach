# 第 2 天：匹配报告主链路的敏捷迭代

预计 6-8 小时。本天在 Day 1 的垂直切片上继续迭代，把固定响应提升为完整、可校验、可诊断的 Fake 匹配报告主链路。

## 本天迭代目标

让 `岗位描述 + 个人经历 -> 匹配报告` 成为稳定的后端业务能力，并完整练习一次需求变更、任务拆分、分层实现、单元测试、接口测试、Review 和回归。

## 阅读与学习清单（约 75 分钟）

### 必读项目文档

- `docs/product/03-functional-requirements.md`：学习匹配报告和准备任务的功能规则。
- `docs/product/05-domain-model.md`：学习 MatchSession、MatchReport、PreparationPlan 和 ExecutionStep 的关系及状态不变量。
- `docs/product/06-api-contract.md`：学习请求字段、响应字段、错误结构和后续接口边界。
- `docs/product/07-error-model.md`：学习输入错误、模型失败、输出失败和工具失败如何分类与恢复。
- `docs/engineering/07-testing-strategy.md`：学习 Fake gateway、API 测试、模型实验和端到端测试的分工。

### 必读代码与测试

- `backend/src/main/java/com/example/jobcoach/web/MatchController.java`：学习 HTTP 层入口和请求校验。
- `backend/src/main/java/com/example/jobcoach/ai/AiGateway.java`：学习如何用端口隔离模型供应商。
- `backend/src/main/java/com/example/jobcoach/ai/FakeAiGateway.java`：学习稳定测试替身的职责。
- `backend/src/test/java/com/example/jobcoach/web/MatchControllerTest.java`：学习如何验证状态码、错误码和关键响应字段。

### 今天必须掌握的概念

分层架构、DTO 与领域对象、Fake gateway、结构化响应契约、错误分类和回归测试。重点理解：Prompt 不能替代 Java 业务校验，HTTP 200 也不能证明报告语义正确。

### 阅读后的输出

画一张“请求到报告”的调用链，并在每一层标出输入、输出、失败方式和测试。再写出三个必须由 Java 保证的规则，作为当天实现和 Review 的检查表。

## 固定 AI coding 循环

```text
读取 Day 1 backlog -> 重新确认优先级 -> 设计最小改动
-> DTO/Domain/Application 实现 -> 单测/API 测试
-> Codex Review -> 修复回归 -> HTTP 验证 -> 更新 backlog
```

## 步骤

### 1. 评审上一轮结果（45 分钟）

1. 阅读 Day 1 会话记录、测试输出、diff 和 backlog。
2. 手工提交正常、空输入和异常样例，确认问题仍可复现。
3. 只选择一个本日目标：稳定匹配报告契约和失败状态。
4. 写出本日不做的内容：真实模型、MySQL、自由 Agent、复杂异步流程。

### 2. 拆分后端任务（45 分钟）

按依赖拆成：

1. 请求 DTO 和输入规则
2. 报告领域对象和不变量
3. Application service 编排
4. Fake gateway 行为
5. 错误映射
6. Controller/API 测试

每项都写影响文件、成功条件、失败场景和回滚方式，交给 Codex 前先人工检查。

### 3. 分阶段实现（150 分钟）

1. 先锁定 DTO、错误 code 和报告字段。
2. 将分数范围、优先级、证据缺失和列表约束放到业务校验，而不是只放 Prompt 或 JSON。
3. 保持 `AiGateway` 为接口，Fake 实现返回稳定 fixture。
4. 将 Controller、Application、Domain 和 adapter 的职责分开。
5. 对每个阶段运行相关测试，失败时记录实际堆栈和根因假设。

### 4. 测试和 Review（120 分钟）

1. 单测覆盖空白输入、边界长度、非法报告和 gateway 异常。
2. HTTP 测试覆盖 200、400、模型失败和输出非法。
3. 检查错误响应是否包含稳定 code，是否泄露原始简历、密钥或上游 header。
4. 让 Codex 以 P0/P1/P2 输出 Review，每个问题必须带文件、场景和验证方式。
5. 修复后运行 `mvn test -q`，再执行完整 HTTP 手工验证。

### 5. 整体联调（45 分钟）

用前端或 curl 走一条真实请求链路，确认请求字段、后端校验、Fake 结果和前端类型一致。发现问题时先判断是契约、HTTP、应用逻辑还是展示错误，再修改对应层。

### 6. 更新项目记录（30 分钟）

更新状态矩阵、API 契约、错误模型、会话记录和下一轮 backlog。将“Fake 已验证”和“真实模型待验证”明确分开。

## 本天交付

- 稳定的 Fake 匹配报告 API
- DTO、Domain、Application、AI gateway 分层实现
- 单元测试和 HTTP 测试
- 一次 Code Review 和修复记录
- Day 3 的真实模型/结构化输出任务

## 本天验收

- `mvn test -q` 通过。
- 不配置真实模型也能通过 HTTP 完成匹配分析。
- 非法输入和模型失败不会产生伪成功报告。
- 能解释每个分层为什么存在以及如何替换 Fake gateway。

## 复盘

1. 哪个问题由测试发现，哪个问题由 Review 发现？
2. 哪个业务规则不能交给模型？
3. Day 3 接入真实模型时，哪些契约必须保持不变？
