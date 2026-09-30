# 领域模型草案

```text
MatchSession
  id
  jobDescription
  profileText (敏感，需限制日志/保留)
  status
  report
  createdAt

MatchReport
  requirements[]
  evidence[]
  skillGaps[]
  risks[]
  recommendations[]

PreparationPlan
  id
  matchSessionId
  status
  tasks[]

ExecutionStep
  matchSessionId
  type (MODEL / TOOL / VALIDATION)
  status
  safeSummary
  startedAt / completedAt
```

该图是设计草案，不是已批准的数据库 schema。先确认隐私和 P0 历史需求，再定表结构。

## 生命周期与不变量

### MatchSession

- `created -> analyzing -> report_ready` 或 `failed`。
- `profileText` 是敏感字段；日志只允许脱敏摘要。
- 报告生成失败时不能标记为 `report_ready`。

### PreparationPlan

- 只能关联一个已校验的 MatchSession。
- `draft -> confirmed -> saved`；用户未确认不能触发持久化副作用。
- 相同来源报告和幂等键不能重复创建相同计划。

### ExecutionStep

- 记录 `MODEL`、`VALIDATION`、`TOOL` 的阶段和状态，不保存任意模型原文。
- 终态只能是 completed、failed、rejected 或 timeout 之一。
- 每个步骤要能解释开始/结束时间和失败原因。

## 建模实施顺序

1. 先用 Java record/内存对象验证状态和不变量。
2. 用 fake repository 测试重启、重复请求和失败恢复。
3. 隐私策略确认后再映射数据库表、索引、级联删除和保留策略。
4. 最后用集成测试验证迁移与 Repository，不从草图直接生成生产 schema。
