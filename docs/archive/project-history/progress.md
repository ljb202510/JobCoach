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
- 用户询问核心目标是否落盘；原内容分散在总计划、产品需求和架构文档。已新增 `docs/plan/PROJECT_GOALS.md` 作为多 Agent/模型共享的目标基准，并链接到 README。
- 用户正式确认产品为“AI 求职教练系统（JobCoach）”。旧方向已归档；核心目标文档、README 和总路线已更新为 JobCoach。
- 已将后端包名、启动类、Maven artifact、fake gateway、匹配 DTO 和 API 切换为 JobCoach 语义。
- 第一次测试因 Spring Boot 4.1.1 测试切片包路径不兼容而失败；改用可用的 SpringBootTest + 纯对象校验测试，`mvn test -q` 已通过。
- 用户要求对齐真实目标，避免项目越做越偏。已将计划改为学习轨/工程轨双轨验收，并在每一天加入学习验收和防偏问题。
- JobCoach 被明确为学习载体，而不是唯一目标；最高目标顺序写入 `docs/plan/PROJECT_GOALS.md`：作品集、AI/Agent 系统理解、Codex 工程化。
- 扫描发现的旧需求术语只保留在归档文件，前端说明已更新为岗位匹配语义。
- 用户进一步明确投入权重：简历项目作品 4、AI 概念/工具/Codex 4、Java 后端 2。已同步写入 `PROJECT_GOALS.md`、`task_plan.md`、总路线和每日任务。
- 后续防偏原则：作品和 AI/Codex 各占约 40%，后端约 20%；后端只实现主线所需深度，不扩张为泛 Java 课程。
- 用户进一步明确：AI/Codex 的 4 成目标包含完整 AI coding 开发流程，而不只是 AI 概念或工具 API。已把上下文准备、任务拆解、设计、实施、调试、测试、diff、Review、文档和复盘写入目标与每日计划。
- 用户指出文档结构混乱。已按实际目录分组：`docs/plan`（目标和每日计划）、`docs/product`（产品/架构/API）、`docs/engineering`（环境/能力/测试）、`docs/ai-coding`（Agent/Codex）、`docs/review`（面试/复盘）、`docs/archive`（废案），并修正导航和入口链接。
- 用户要求先完成基础前置资产。已新增 `docs/concepts/` 核心概念学习包、`docs/ai-coding/00-10` AI coding 全流程文档、`docs/product/00-09` 产品规格文档、`docs/engineering/` 版本/模型/依赖/多 Agent 文档，并补充后端/前端稳定目录骨架。
- 用户确认学校 Qwen 服务此前已有 Spring Boot 三模型调用成功经验；已修正文档，明确“本项目尚未复现”不等于“服务不可用”，并新增 `docs/engineering/status-matrix.md` 区分事实、用户确认、待验证和暂缓。
- 用户暂时没有时间参与交互，希望先完成不需要确认且可消耗大量 token 的前置工作。已新增前置资产计划和后续 Agent 接手简报，明确可直接完成与禁止擅自完成的范围。
- 前置准备补充：新增 AI coding 会话记录模板、前置资产检查表、产品待确认项索引；状态矩阵增加文档资产状态，明确规格草案和实际验证状态。
- 验证：后端 `mvn test -q` 退出码 0。注意本次从仓库根目录执行的文档文件计数命令由于工作目录在 backend，第一次返回 0；随后已从项目根目录重新检查，当前 docs 下共有 65 个 Markdown 文件。
- 用户希望独立完成可耗时的结构性工作；已补齐后端分层骨架、领域端口、统一错误模型、AI 配置属性、数据库迁移占位，以及 Vue/Vite/TypeScript 前端壳和类型化 API 客户端。
- 验证：后端 `mvn test -q` 退出码 0；前端 `npm install` 无漏洞，`npm run build` 通过。首次前端构建因缺少 Node 类型和 TS lib 配置失败，已补齐并记录。
- 阶段 2 骨架验收完成：后端分层端口、统一错误、配置属性、迁移占位、Vue/Vite 壳、前端类型和 API client 已生成；`task_plan.md` 阶段 2 标记 complete。
- 明确下一阶段仍需用户参与模型环境确认或授权后，才执行真实模型实验；本轮没有接入密钥、数据库或外部副作用。
- 清理项目命名和文档导航：MySQL/Compose 统一为 JobCoach 命名；产品、AI coding 和计划目录中的重复编号改为语义化文件名；更新文档统计、前端 README 和目标文件路径。
- 清理验证：旧名称/旧路径无残留，文档目录无重复数字前缀，Markdown 相对链接无断链；后端 `mvn test -q` 和前端 `npm run build` 均通过。
- 新一轮文档深化任务开始：用户反馈每日任务、核心概念和计划/方案过于简略。已读取 planning-with-files 规则，并建立阶段 7 追踪项；下一步先盘点各类文档结构，再分批扩写。
- 已完成每日任务深化：`day-1-tasks.md` 至 `day-5-tasks.md` 统一补充前置阅读、时间块、逐步操作、产出、验收、失败处理和复盘问题。
- 已完成核心概念深化：12 篇主题补充最小实验、JobCoach 映射、边界/误区、复盘问题；`concepts/README.md` 增加学习方法、依赖关系和完成标准。
- 已完成计划/方案深化：总路线、目标基准、30 天路线、前置资产、产品需求/架构/API、工程测试/能力探测、AI coding 方法文档均补充决策步骤、质量门禁和证据要求。
- 当前收尾：检查 Markdown 格式、路径引用、文档统计和 Git diff；本轮只修改文档与规划记录，未修改应用代码。
- 文档校验：`docs` 下 77 个 Markdown 文件；无空文件、无尾随空格、无断开的相对链接。
- 回归验证：后端 `mvn test -q` 通过；前端 `npm run build` 通过。测试输出包含 Mockito/JDK 动态 Agent 警告，但未导致失败。
- 追加产品方案深化：`product/00-08` 补充用户问题、状态流、MVP 分阶段、功能/非功能验收、领域生命周期、API 不变量、错误处理和演示前检查。
- 按用户要求二次吸收四篇参考文章：补充 Codex 工作区/Thread、Plan/Status、UI 检查和并行任务；Skills 的 SOP、`SKILL.md`、description、目录资源和渐进式披露；九条 AI 使用原则；以及研究/方案审查/分支/测试/CI/部署/文档同步/人工确认的完整长任务闭环。
- 用户进一步明确总体方法：AI coding 是唯一主线，JobCoach MVP 是最终交付；RAG、Skill、Tool Calling、MCP、Agent 等概念在真实开发节点按需学习和使用；五天按连续敏捷迭代推进，而不是把概念学习、作品开发和 Java 学习拆成三条平行路线。已同步 `PROJECT_GOALS.md`、`00-learning-roadmap.md`、`task_plan.md`、五天任务和文档导航。
- 用户指出五天任务仍只是原有分工加说明，未真正体现主线。已重写 `day-1` 至 `day-5`：每天都包含需求评审、优先级、Codex 上下文/Plan、实现、测试、Review、联调/手工验证和下一轮 backlog；每天只按当轮交付功能区分。
- 五天任务校验：5 个文件共 413 行；计划目录相对链接无断链，`git diff --check` 通过。
- 按用户要求为每一天增加独立的“阅读与学习清单”：明确必读项目文档、代码/测试入口、当天概念重点和阅读后的输出；并修正 Day 4 的 Vite 配置路径引用。
- 每日任务引用路径校验通过，确认五天清单中的文档、代码和测试入口均指向现有文件；文档改动未涉及应用代码。

## 2026-10-01：项目状态与文档基线重整

### 当前事实

- JobCoach 的 Spring Boot Fake 后端和 Vue Fake 页面闭环已经实现，并有 `mvn test -q`、`npm run build` 证据。
- 用户已安装 MySQL 8.4.9，并创建数据库 `jobcoach`；这是本机环境事实，不代表应用已经接入 MySQL。
- 环境复查：Windows 服务 `MySQL84` 当前为 `Stopped`，但 `127.0.0.1:3306` 可连接；接入持久化前需确认 3306 实际对应的实例和数据库。
- MySQL schema 仍是设计草案，尚未接入迁移工具、Repository、事务、删除策略和集成测试。
- 真实模型、结构化输出、Tool Calling、RAG、流式输出、登录、文件解析和多 Agent 产品能力仍需分别验证或暂缓。

### 本轮文档变更

- 重写根 `README.md`，定位为作品集项目入口，加入产品目标、架构、状态、技术亮点、限制和文档导航。
- 新增 `docs/engineering/02-local-development.md`，集中说明环境、测试、启动、Fake 演示、本机 MySQL、Docker 选择和常见故障。
- 更新 `docs/README.md`、`docs/engineering/01-environment-setup.md`、`docs/engineering/status-matrix.md`、`docs/engineering/code-skeleton-status.md` 和 `docs/product/11-implementation-boundaries.md`，消除“未安装 MySQL”和“前端未实现”等过时表述。
- 更新 `task_plan.md`，记录新的本机 MySQL 基线和当前阶段状态。

### 高 Token 消耗事项

| 等级 | 事项 | 主要消耗来源 | 前置条件 |
|---|---|---|---|
| XL | 真实模型能力与 Java AI 适配 | 官方文档、依赖兼容、真实错误、解析测试、降级和回归 | endpoint、模型名和环境变量中的 Key |
| XL | MySQL 持久化闭环 | 迁移、Repository、事务、删除策略、隐私评审和集成测试 | MySQL 连接、普通应用账号、迁移方案 |
| XL | RAG 对照实验 | 数据准备、关键词基线、检索候选、质量/延迟/成本对比 | 真实模型和最小评测集 |
| L | Tool Calling 与任务保存 | 白名单、参数校验、确认、幂等、权限和执行记录 | 稳定领域模型与持久化边界 |
| L | Vue 前后端端到端联调 | 跨层状态、错误恢复、浏览器请求、重复回归 | 稳定 API 和错误契约 |
| L | 整体 Review、回归和交付材料 | 需求/代码/安全 Review、干净环境重跑、文档同步 | 核心行为已实现 |
| M | Skill、MCP、Agent、Workflow 实操 | 概念绑定真实任务、权限边界和可回放记录 | 有明确的开发流程和实验目标 |

这些等级表示上下文、实验和调试复杂度，不以 Token 数量作为进度指标。所有事项都必须保留 Fake 回归路径，真实能力失败时不能让项目失去可演示基线。

### 下一步入口

1. 先按 `docs/engineering/02-local-development.md` 从干净终端重跑后端测试、前端构建和 Fake 演示。
2. 再核对本机 MySQL 服务、3306 端口和 `jobcoach` 数据库，不执行 schema 迁移。
3. 进入真实模型能力探测前，先确认 endpoint、模型名、密钥注入方式和日志脱敏策略。

## 2026-10-01：文档清理与状态统一

- 确认并执行五天 MVP 不持久化、MySQL 进入后续 30 天路线的决策。
- 更新工程状态、产品流程/MVP/API/错误模型、目标与每日任务、README、前置检查表、技术亮点和测试策略。
- 统一 `BODY_INVALID`、`INPUT_INVALID`、`MODEL_UNAVAILABLE`、`MODEL_TIMEOUT`、`OUTPUT_INVALID`、`TOOL_FAILED` 错误命名；保留历史日志和草案。
- 当前阶段验证待完成：旧残留/链接/编号扫描、后端测试、前端构建和 `git diff --check`。
- 验证完成：docs 共 78 篇且统计一致；Markdown 相对链接无断链；重复数字前缀无重复；旧错误码/过期联调表述无残留；`mvn test -q`、`npm run build`、`git diff --check` 均通过。Maven 输出仅有 Mockito/JDK 动态 Agent 警告。
