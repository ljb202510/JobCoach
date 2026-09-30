# Backend

Spring Boot 后端。当前默认使用 `fake` profile，提供可重复验证的岗位匹配 API；真实模型集成会在 LangChain4j 兼容性核验后加入。

运行：`mvn test`；启动：`mvn spring-boot:run`。核心接口为 `POST /api/matches`，健康检查为 `GET /api/health`。

计划分层：`web`、`application`、`domain`、`ai`、`tool`、`persistence`、`config`。当前只实现能支撑匹配 API 和测试的最小部分，后续按实际用例扩展。

当前分层状态：`web` 已有健康检查、匹配入口、统一错误处理和开发 CORS；`application` 有匹配用例；`domain` 有报告/会话/计划记录；`ai` 只有 fake gateway；`tool` 和 `persistence` 只有端口接口。MySQL schema 草案位于 `src/main/resources/db/migration/schema.sql`，尚未接入启动流程。
