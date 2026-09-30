# Java AI 应用架构

## 建议职责边界

- Web 层：请求/响应 DTO、输入校验和状态码。
- Application 层：用例编排、事务和执行状态。
- Domain 层：匹配报告、技能差距、准备计划等业务规则。
- AI adapter：把领域用例映射为模型请求并解析结果。
- Tool adapter：显式暴露允许模型调用的能力。
- Persistence adapter：保存业务和执行记录。

## 依赖方向

业务用例依赖接口；LangChain4j、HTTP 和 MySQL 位于基础设施适配器。这样可使用 fake gateway 测试业务，亦可替换 provider。

## JobCoach 取舍

五天项目保持单体 Spring Boot，不引入微服务、事件总线和复杂模块系统。层次只在能提升测试性和解释性时建立，避免空壳抽象。

## 逐层实现步骤

1. 从 Web 契约开始：定义请求、响应、错误码、校验和状态码；先写 API 测试。
2. 在 Application 层写一个用例，编排输入清洗、AI 调用、结果校验、用户确认和保存。
3. 在 Domain 层放分数、证据、优先级、任务状态和幂等等业务规则，不依赖 Spring 或 provider SDK。
4. 用 `AiGateway`、Repository 和 Tool port 隔离外部系统；分别提供 fake 和真实 adapter。
5. 在基础设施层处理 HTTP、模型 SDK、MySQL、日志和超时，不把这些细节倒灌到业务层。
6. 从一次端到端请求反向检查依赖方向；如果 Domain 需要导入 Web/SDK 类型，记录并修正。

## 结构验证清单

- Web 层能否在不启动真实模型时做请求校验？
- Application 层是否能用 fake gateway 完成用例测试？
- Domain 测试是否不需要 Spring 容器？
- adapter 是否把超时、错误和原始响应转换成稳定的应用错误？
- 数据库迁移、日志和配置是否有独立验证步骤？

## 复盘问题

1. 哪一个边界最能降低真实模型不稳定带来的影响？
2. 如果暂时不接 MySQL，哪些接口仍可以用内存 fake 验证？
3. 目录分层如何避免“文件夹看起来整洁但规则仍散落在 Controller”？
