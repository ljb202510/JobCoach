# Backend

Spring Boot 后端。当前默认使用 `fake` profile，提供可重复验证的岗位匹配 API；真实模型集成会在 LangChain4j 兼容性核验后加入。

运行：`mvn test`；启动：`mvn spring-boot:run`。核心接口为 `POST /api/matches`，健康检查为 `GET /api/health`。
