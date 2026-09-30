# 进度日志

## 2026-09-30

- 收到用户确认：从空目录开始，前端使用 Vue，第一版采用 LangChain4j，暂不安装 Docker/MySQL。
- 检查工作区：仅有两份既有 Markdown，无 Git 仓库。
- 读取并启用 planning-with-files 规则。
- 已记录 Java、Maven、Git、Node、npm、pnpm 版本。
- 下一步：创建总计划、环境安装文档、架构和 API 设计文档，以及最小项目目录骨架。
- 已完成：新增 `docs/` 总计划、环境安装、需求、架构、API、模型能力、Agent、测试、Codex/MCP、面试和复盘文档。
- 已完成：新增 Docker Compose MySQL 草案、`.env.example`、根 README、后端接口骨架和前端说明。
- 修复：发现批量补丁曾将文件写到工作区上级目录，已将本轮新建文件移动回 `AI` 项目目录。
- 官方页面可达性已检查；依赖版本仍待阅读兼容要求后确定。
- 已新增 `day-1-tasks.md` 至 `day-5-tasks.md`，补齐每日任务、耗时和验收标准。
- 已新增 Spring Boot 4.1.1 后端最小骨架、健康检查接口和 Maven 配置。
- 已运行 `mvn test -q`，退出码 0。
- 下一步：核验 LangChain4j 与 Spring Boot 4 的集成方式，再加入 AI 依赖和分析 API。
- 用户询问核心目标是否落盘；原内容分散在总计划、产品需求和架构文档。已新增 `docs/PROJECT_GOALS.md` 作为多 Agent/模型共享的目标基准，并链接到 README。
- 用户正式确认产品为“AI 求职教练系统（JobCoach）”。旧方向已归档；核心目标文档、README 和总路线已更新为 JobCoach。
- 已将后端包名、启动类、Maven artifact、fake gateway、匹配 DTO 和 API 切换为 JobCoach 语义。
- 第一次测试因 Spring Boot 4.1.1 测试切片包路径不兼容而失败；改用可用的 SpringBootTest + 纯对象校验测试，`mvn test -q` 已通过。
- 用户要求对齐真实目标，避免项目越做越偏。已将计划改为学习轨/工程轨双轨验收，并在每一天加入学习验收和防偏问题。
- JobCoach 被明确为学习载体，而不是唯一目标；最高目标顺序写入 `docs/PROJECT_GOALS.md`：作品集、AI/Agent 系统理解、Codex 工程化。
- 扫描发现的旧需求术语只保留在归档文件，前端说明已更新为岗位匹配语义。
- 用户进一步明确投入权重：简历项目作品 4、AI 概念/工具/Codex 4、Java 后端 2。已同步写入 `PROJECT_GOALS.md`、`task_plan.md`、总路线和每日任务。
- 后续防偏原则：作品和 AI/Codex 各占约 40%，后端约 20%；后端只实现主线所需深度，不扩张为泛 Java 课程。
- 用户进一步明确：AI/Codex 的 4 成目标包含完整 AI coding 开发流程，而不只是 AI 概念或工具 API。已把上下文准备、任务拆解、设计、实施、调试、测试、diff、Review、文档和复盘写入目标与每日计划。
- 用户指出文档结构混乱。已按实际目录分组：`docs/plan`（目标和每日计划）、`docs/product`（产品/架构/API）、`docs/engineering`（环境/能力/测试）、`docs/ai-coding`（Agent/Codex）、`docs/review`（面试/复盘）、`docs/archive`（废案），并修正导航和入口链接。
