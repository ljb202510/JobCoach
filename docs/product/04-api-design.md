# 04 API 设计草案

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
