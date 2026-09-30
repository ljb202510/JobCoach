# API 契约草案

## 生成匹配报告

`POST /api/matches`

```json
{
  "jobDescription": "Java 后端工程师，要求 Spring Boot 和 AI 应用经验",
  "profile": "软件工程大四，做过 Java Web 项目"
}
```

当前 skeleton 响应直接返回 MatchReport。后续接入持久化后再考虑包装 `executionId` 和状态，不在没有行为需要时先造复杂契约。

成功响应示例：

```json
{
  "matchScore": 72,
  "requirements": [{"name": "Java", "importance": "HIGH"}],
  "evidence": [{"requirement": "Java", "evidence": "个人经历中包含 Java 后端项目", "matched": true}],
  "skillGaps": [{"skill": "Spring Boot", "reason": "缺少可验证的项目细节", "priority": "HIGH"}],
  "risks": ["当前报告由 fake gateway 生成，仅用于业务链路测试"],
  "recommendations": ["补充 Spring Boot 项目的职责、接口和测试证据"]
}
```

字段校验或 JSON 解析失败均返回 `400`，但错误码区分来源：

```json
{"code":"INPUT_INVALID","message":"请求字段不符合要求","path":"/api/matches","timestamp":"2026-09-30T00:00:00Z"}
```

非法 JSON 请求体返回 `BODY_INVALID`；空字段、超长字段和其他业务输入校验失败返回 `INPUT_INVALID`。

## 后续候选接口

- `POST /api/matches/{id}/preparation-plan`：用户确认后生成并保存计划。
- `GET /api/matches/{id}`：查看报告和执行记录。
- `PATCH /api/preparation-plans/{id}/tasks/{taskId}`：更新任务状态。

## 契约实施顺序

1. 先锁定 `POST /api/matches` 的请求校验、Fake 成功响应和统一错误。
2. 为模型失败、超时、非法结构和重复提交补 HTTP 测试与前端错误映射。
3. 用户确认保存计划后，再新增候选接口；接口必须携带来源报告、幂等键或等价防重复机制。
4. 只有出现持久化历史和异步执行需求时，才引入 execution ID、查询接口和状态轮询。
5. 每次契约变更同步 API client 类型、演示脚本、错误模型和实现边界。

## 契约不变量

- 成功响应不能包含未经过领域校验的模型字段。
- 错误响应稳定包含 `code`、`message`、必要的 `path` 和时间；不包含密钥和上游敏感头。
- 可重试错误要保留用户输入；不可重试/需确认错误要告诉用户下一步。
- 需要副作用的接口必须有明确用户动作，不能由页面加载或模型自动触发。
