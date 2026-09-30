# 研究与发现

## 当前工作区

- 工作区为 `C:\Users\Administrator\Desktop\newjob\AI`。
- 初始目录只有两份既有学习资料：`AI工具使用教程.md`、`SpringCloudAlibaba与AI核心概念.md`。
- 初始目录不是 Git 仓库，尚未创建项目文件。

## 已确认环境

- Java：OpenJDK 21.0.12.1 LTS，Temurin。
- Maven：Apache Maven 3.9.10，命令为 `mvn`。
- Git：2.55.0.windows.3。
- Node.js：v20.18.2。
- npm：10.8.2。
- pnpm：9.15.9。
- Windows 10 amd64。

## 模型渠道信息（来自用户提供的截图）

- `qwen`：学校服务，默认模型 `Qwen3.5`，地址通过 `NSCC_API_URL` 配置。
- `siliconflow`：默认模型 `THUDM/GLM-4-9B-0414`，地址 `https://api.siliconflow.cn/v1`。
- `deepseek`：默认模型 `deepseek-v4-flash`，地址 `https://api.deepseek.com/v1`。
- 这些信息只说明配置入口，尚未证明任何渠道支持工具调用、流式、结构化输出或 Embedding。

## 技术判断

- Vue 3 比切换 React 更适合当前五天目标，因为学习者已有 Vue 经验。
- LangChain4j 更直接覆盖 Java Agent/RAG 概念；Spring AI 更贴近 Spring 生态。第一版采用 LangChain4j，保留 `AiGateway` 抽象以便后续比较。
- Docker 和 MySQL 安装统一放入环境文档；暂不在本轮安装。

## 官方文档可达性

- Spring Boot 官方系统要求页面可访问：`https://docs.spring.io/spring-boot/system-requirements.html`。
- LangChain4j 官方入门页面可访问：`https://docs.langchain4j.dev/intro`。
- 本轮只确认页面可访问，尚未根据页面内容选择具体依赖版本；版本核验将在生成 `pom.xml` 前完成。

## 产品方向变更

- 用户已正式确认项目名称：中文“AI 求职教练系统”，英文“JobCoach”。
- 早期“AI 需求分析与研发任务助手”仅是助手提出的未确认假设，已归档，不再作为产品需求。
- 当前产品主线是：岗位描述 + 个人经历 -> 匹配分析 -> 技能差距 -> 准备任务 -> 保存任务。

## 阶段 2 构建发现

- Spring Initializr 官方元数据当前返回 Spring Boot `4.1.1.RELEASE`，支持 Java 21。
- 后端最小 Spring Boot 4.1.1 骨架已通过 `mvn test`，退出码为 0。
- LangChain4j 依赖尚未加入，避免在未核验其 Spring Boot 4 集成方式前锁定错误 starter。

## 命名与文档约定

- MySQL 本地资源统一使用 JobCoach 命名：数据库 `jobcoach`、用户 `jobcoach_app`、容器 `jobcoach-mysql`、数据卷 `jobcoach_mysql_data`。
- 产品主规格使用 `00` 到 `09` 的唯一编号；横向概览使用语义化文件名，不占用规格编号。
- AI coding 和计划目录同样避免重复数字前缀，导航中的路径必须与实际文件一致。

## 文档深度盘点（2026-09-30）

- 每日任务文件已覆盖主题、耗时和粗粒度验收，但多数任务缺少“先读什么、具体执行顺序、命令、产出文件、失败处理和口头复述”字段。
- `docs/concepts/` 已有 12 篇主题，但主要是概念提纲；需要补齐统一的最小示例、JobCoach 对应代码位置、验证实验和局限。
- `docs/plan/`、`docs/product/`、`docs/engineering/` 和 `docs/ai-coding/` 已形成目录骨架，但部分方案只记录结论，缺少决策依据、替代方案、实施步骤和回滚/验收路径。
- 本轮扩写应保持当前产品边界：JobCoach 仍以岗位描述 + 个人经历 → 匹配分析 → 技能差距 → 准备任务为主线，真实模型、工具副作用和 MySQL 持久化仍需单独验证，不应在文档中伪装成已完成。

## 文档清理发现（2026-10-01）

- 当前 `docs/` 共 78 篇 Markdown：ai-coding 17、ai-experiments 4、engineering 12、plan 10、product 16、review 4、concepts 13、archive 1。
- Fake 前后端闭环已存在：Vue 页面调用 `POST /api/matches`，有加载、错误、空状态和报告展示；旧的“业务 API 联调待后续”表述已过期。
- 非法 JSON 的实际错误码是 `BODY_INVALID`；字段校验实际错误码是 `INPUT_INVALID`。用户流程中的 `MODEL_FAILED`、`SAVE_FAILED` 已统一到错误模型中的 `MODEL_UNAVAILABLE`/`MODEL_TIMEOUT`/`TOOL_FAILED`。
- 五天 MVP 不持久化；MySQL、报告/计划保存、历史查询和数据保留策略统一归入后续 30 天路线。服务重启后不保留历史是当前明确限制。
- `docs/README.md` 原有统计和章节顺序过期，且引用了不存在的 `review/09-interview-notes.md` 与 `review/10-retrospective-template.md`；已按实际文件修正。

## 四篇参考文章的内容映射（2026-09-30）

- Codex 入门文：工作区/文件夹负责文件，Thread 负责单一任务上下文；大任务先 Plan、再执行；页面任务要看真实 UI；可并行但受人工 Review 能力限制；模型版本、价格和平台开关属于时效性个人体验。
- Skills 文：Skill 是给 Agent 使用的流程能力包，不是一次性 Prompt；`SKILL.md` 是必需入口，包含元信息和操作主体；目录可带 references/templates/scripts；description 影响触发，正文按渐进式披露按需加载；MCP 解决外部能力/权限连接，不等同于 Skill。
- AI 使用心得文：选能力足够的模型建立基线、每周自动化一个重复任务、用背景/目标/约束/输出格式进行“实习生式”表达、建立“AI 能帮我吗”触发器、通过创造学习、警惕模型正反馈、不过度等待、培养人的判断、把效率还给现实生活。
- 长流程文：方案初稿需独立审查；大任务可用分支/工作树隔离；测试和验证是核心瓶颈；完整闭环包括 CI、部署检查、文档/记忆同步、人工确认和清理；长时间运行、模型版本和并行数量是作者个人经验，不是项目质量指标。
