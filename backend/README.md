# Backend

Spring Boot 后端提供 `POST /api/matches`、`POST /api/preparation-plans/preview` 和 `GET /api/health`。默认 `AI_PROVIDER=fake`，返回固定演示报告和规则预览；设置 `AI_PROVIDER=real` 后使用 Spring `RestClient` 调用兼容 OpenAI Chat Completions 的模型服务。真实匹配与只读工具调用的脱敏实验见 `docs/ai-experiments/model-experiment.md`。

```powershell
mvn test -q
mvn spring-boot:run
```

真实模式需要 `AI_BASE_URL`（基础地址，例如以 `/v1` 结尾）、`AI_MODEL`（供应商提供的准确模型名）和供应商要求的 `AI_API_KEY`；请求路径会追加 `/chat/completions`。配置可放在项目根目录的 `.env`，从 `backend` 目录启动时读取；系统环境变量优先。只设置 `SPRING_PROFILES_ACTIVE=real` 不会选择真实 gateway。完整配置见 `docs/engineering/02-local-development.md`。

`web` 包含 API 与统一错误处理，`application` 包含匹配用例，`ai` 包含 Fake/Real gateway。`tool` 已实现仅预览的受控工具；`persistence` 仍只有端口接口，MySQL schema 草案尚未接入启动流程。
