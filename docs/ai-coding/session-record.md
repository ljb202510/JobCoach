# AI Coding 会话记录

每次 AI coding 任务按下面的模板记录证据；末尾一节是本次 Fake 闭环的完整示范。

## 空白模板

### 任务

日期：
目标：
所属权重：作品 / AI coding / Java 后端

### 1. 检查现状

- 读取的文件/命令：
- 关键事实：
- 未知和假设：

### 2. 上下文与拆解

- 给 AI 的上下文：
- 任务拆分：
- 非目标：
- 验收命令：

### 3. 方案和实施

- AI 提出的方案：
- 人工选择及理由：
- 实际修改文件：

### 4. 验证与调试

- 执行命令/退出码：
- 失败证据：
- 根因假设和实验：
- 修复：

### 5. Diff Review

- 行为检查：
- 边界/安全检查：
- 测试缺口：
- 无关改动：

### 6. 复盘

- 我能解释什么：
- AI 哪些输出被修正：
- 下次如何改进：

## 示范：Fake 模式前后端闭环

### 任务

- 日期：2026-09-30
- 目标：让 Vue 输入通过 `POST /api/matches` 获取 Fake 匹配报告，并覆盖加载、错误、空状态。
- 所属权重：作品 4 / AI coding 4 / Java 后端 2

### 1. 检查现状

- 读取：`frontend/src/App.vue`、`frontend/src/api/client.ts`、`MatchController`、`FakeAiGateway`、`application.yml`、已有测试。
- 事实：后端已有 Fake gateway 和 DTO；前端按钮只更新 skeleton 状态；测试没有真实 HTTP 请求。
- 非目标：真实模型、MySQL 启动、文件上传和生产认证。

### 2. 拆解与决策

1. 前端调用 API，按状态渲染报告。
2. 后端补 CORS 和非法 JSON 错误码。
3. 用随机端口 Java HTTP Client 验证真实 HTTP、校验错误和 CORS。
4. 补 Fake gateway/application service 单测。
5. 文档资产只记录已执行事实，不填写模型指标。

### 3. 实施

- 修改 `frontend/src/App.vue`、`frontend/src/api/client.ts`、`frontend/src/styles.css`。
- 新增 `WebConfiguration`；扩展 `GlobalExceptionHandler`；更新 API/README。
- 新增 `MatchControllerTest` HTTP 场景、`FakeAiGatewayTest`、`MatchAnalysisServiceTest`。
- 新增 schema、隐私、AI 实验夹具和 coding 模板。

### 4. 调试与验证

- `mvn test -q`：首次测试编译因 Java text block 写在同一行失败；改为普通 JSON 字符串后通过。
- 第二次尝试使用 `AutoConfigureMockMvc`/`TestRestTemplate`，当前构建依赖未暴露对应测试包；改用 JDK `HttpClient`，仍然完成真实 HTTP 验证。
- `npm run build`：通过。
- PowerShell 解析检查：通过；未执行真实模型请求。

### 5. Review

- 成功、加载、错误、空状态均有路径；按钮在输入为空或请求中禁用。
- 错误响应只包含 code/message/path/timestamp，不记录原始输入。
- Fake 分数是演示夹具，README 和演示脚本明确标注，不当作模型效果。
- 待补：真实 gateway、持久化集成测试、工具调用执行策略。

### 6. 复盘

先读代码和跑基线让修改范围可控；遇到测试库版本差异时，以 JDK 标准 HTTP 客户端完成相同验收目标。下一轮应先锁定 Spring Boot 测试依赖，再决定是否引入 MockMvc。