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
