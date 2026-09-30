# API 设计概览

这是一份跨文档 API 设计概览；当前可验证的请求/响应以 [API 契约](06-api-contract.md) 为准。

`POST /api/matches`：请求岗位描述和个人经历文本，响应岗位要求、匹配证据、技能差距、风险和建议。

```json
{
  "jobDescription": "Java 后端工程师，要求 Spring Boot 和 AI 应用经验",
  "profile": "软件工程大四，完成过 Java Web 项目"
}
```

第一版暂不返回持久化的执行 ID；持久化将在 MySQL 环境可用后加入。

`GET /api/executions/{executionId}`：后续阶段查询执行记录。

`GET /api/project-rules`：查询项目规则；第一版可返回固定规则，后续迁移到 MySQL 或 RAG。

## API 设计步骤

1. 先写用户动作和状态转换，再决定是否新增 endpoint；不要为未来功能提前造完整 REST 资源。
2. 为每个 endpoint 定义请求字段、长度、错误 code、成功响应、幂等行为和日志脱敏规则。
3. 用 fake gateway 写成功/失败/非法输出测试，再用真实 HTTP 测试状态码和错误契约。
4. 只有出现跨请求状态、查询、重试或用户确认需求时，才引入 `executionId`、历史接口或异步状态。
5. API 变更必须同步前端类型、README、演示脚本和错误模型，并记录兼容/迁移策略。

## 当前接口验收顺序

1. `POST /api/matches`：输入校验和 fake 成功响应。
2. 模型失败/超时/非法结构：错误 code、可重试性和无伪造报告。
3. 准备计划确认接口：用户确认、幂等和工具执行记录。
4. 查询接口：只有持久化和历史需求确认后加入，避免提前扩大范围。
