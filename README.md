# JobCoach：AI 求职教练系统

JobCoach 是一个以 Java 为主线的 AI 求职准备系统，帮助求职者把目标岗位和个人经历转化为可解释的匹配分析、技能差距和下一步准备任务。

```text
岗位描述 + 个人经历
        -> 匹配分析
        -> 证据与技能差距
        -> 准备建议
        -> 任务保存（后续阶段）
```

## 项目定位

这是一个面向求职者的可演示 MVP，也是 AI coding、Java AI 和 Agent 工程实践的学习载体。项目重点不是堆叠框架，而是完整走通：需求评审、任务拆解、方案设计、渐进实现、测试、Review、联调、复盘和文档同步。

## 当前状态

当前已经完成并验证：

- Spring Boot 4.1.1 + Java 21 后端骨架。
- Fake 模式 `POST /api/matches` 匹配接口。
- 输入校验、统一错误响应和健康检查 `GET /api/health`。
- Vue 3 + TypeScript + Vite 页面骨架。
- Fake API 的提交、加载、成功、空状态、错误和结构化报告展示。
- 后端 `mvn test -q` 和前端 `npm run build` 验证。

当前环境已经具备 MySQL 8.4.9，但数据库功能仍处于设计和接入前阶段。已有 schema 是草案，尚未接入迁移、Repository、事务和集成测试。

尚未完成或仍需单独验证：

- 真实模型 gateway、结构化输出和模型能力评测。
- LangChain4j 与当前 Spring Boot 版本的集成验证。
- 受控 Tool Calling 和准备任务保存。
- MySQL 持久化、删除策略和隐私评审。
- RAG、流式输出、登录、文件解析和多 Agent 产品能力。

详细状态见 [`docs/engineering/status-matrix.md`](docs/engineering/status-matrix.md) 和 [`progress.md`](progress.md)。

## 技术结构

```text
Vue 3 / TypeScript / Vite
            |
        REST API
            |
Spring Boot Web -> Application -> Domain
            |                         |
       Fake AI gateway       AI / Tool / Persistence ports
                                      |
                         MySQL adapter（后续阶段）
```

技术基线：Java 21、Spring Boot、Vue 3、TypeScript、Vite、MySQL。LangChain4j、迁移工具和数据库适配器会在兼容性与最小实验通过后再加入，避免把未验证的依赖写成既定事实。

## 作品集亮点

- 用端口和适配器边界隔离 Fake AI 与真实模型，核心业务可以脱离外部网络测试。
- 对模型输出、错误状态和工具副作用保留明确的业务边界，不把模型回答当作可信事实。
- 前端和后端都覆盖可观察状态，包含输入失败、加载、成功和服务错误。
- 以测试、实验记录、状态矩阵和 Review 证据区分“已实现”“已验证”和“待验证”。
- 将 Prompt、Structured Output、Tool Calling、RAG、Workflow 和 Agent 放入真实迭代中按需验证，而不是独立堆砌概念。

## 文档入口

- [本地运行说明](docs/engineering/02-local-development.md)：环境检查、测试、启动和 Fake 演示。
- [项目目标](docs/plan/PROJECT_GOALS.md)：产品目标、4:4:2 投入权重和完成判据。
- [项目进度](progress.md)：当前状态、下一步和高 Token 消耗事项。
- [文档导航](docs/README.md)：计划、产品、工程、概念、AI coding 和 Review 材料。
- [实现边界](docs/product/11-implementation-boundaries.md)：已实现、未实现和当前局限。

## 学习与交付路线

五天主线围绕同一个 MVP 连续迭代：

1. 需求、边界、验收标准和 AI coding 工作方式。
2. Fake 后端主链路。
3. 真实模型能力和受控工具。
4. Vue 联调、端到端验证和 RAG 对照实验。
5. 整体 Review、回归、文档和下一轮 backlog。

后续 30 天路线会依次扩大真实模型、MySQL、工具、评测和交付边界；每周只扩大一个主要行为范围。

## 安全与可信边界

- 不要提交真实 API Key、数据库密码或 `.env` 文件。
- 模型服务只通过环境变量配置。
- OpenAI 格式兼容不等于结构化输出、Tool Calling、流式和 Embedding 都可用，必须逐项验证。
- 岗位描述和个人经历可能包含个人敏感信息；持久化、日志、备份和删除策略需要在数据库接入前确认。
- Fake 报告只用于稳定演示，不代表真实模型质量。

## License

当前项目用于学习、求职作品和工程实践，许可证和生产部署范围尚未确定。
