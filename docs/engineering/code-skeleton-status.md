# 代码骨架状态

## 已实现

- Spring Boot application entry point.
- Health endpoint: `GET /api/health`.
- Fake match endpoint: `POST /api/matches`.
- Validation and generic API error shape.
- Application service boundary and `AiGateway` port.
- Domain records for match session, report and preparation plan.
- Tool and persistence ports without side effects.
- AI configuration properties read from environment variables.

## 仅骨架/端口

- Real LangChain4j gateway.
- Preparation plan tool implementation.
- MySQL repository and execution recorder.
- Real model gateway and persistence-backed business actions.

The frontend shell, types, API client and Fake interactive states are present. The page already calls `POST /api/matches` and renders loading, error, empty and report states. Real model, persistence-backed actions and preparation-plan business integration remain unimplemented.

## 明确未实现

- Real model request.
- Database writes.
- Authentication, file upload, job crawling, multi-agent orchestration.

## 验收命令

```powershell
cd backend; mvn test
cd ../frontend; npm run build
```

截至当前骨架阶段，两条命令均已通过。
