# 本地开发与运行说明

本文用于在 Windows PowerShell 中检查环境、运行测试和演示 Fake/Real 模式。真实模型已有少量脱敏实测记录，长期稳定性仍需观察；MySQL 持久化尚未接入。

## 环境要求

| 工具 | 要求/当前版本 |
|---|---|
| Java | OpenJDK 21.0.12.1 LTS |
| Maven | 3.9.10，命令为 `mvn` |
| Node.js | 20.18.2 |
| npm | 10.8.2 |
| Git | 2.55.0.windows.3 |
| MySQL | 8.4.9，本机环境已具备，持久化功能尚未接入 |

检查命令：

```powershell
java -version
mvn -version
node --version
npm --version
git --version
```

Docker Desktop 不是当前 Fake 模式的必要条件。项目保留 `docker-compose.yml` 作为可选 MySQL 方案；本机 MySQL 和 Compose MySQL 不应默认同时占用 3306 端口。

## 当前 Fake 模式

Fake 模式不需要 MySQL、AI API Key 或外部网络，可以先验证核心业务和页面状态。

### 后端测试与启动

```powershell
cd C:\Users\Administrator\Desktop\newjob\AI\backend
mvn test -q
$env:AI_PROVIDER = 'fake'
mvn spring-boot:run
```

进程环境变量会覆盖根目录 `.env`。此窗口用于 Fake 验证；切回 Real 时关闭它，在新的 PowerShell 窗口启动后端，避免继续沿用 `AI_PROVIDER=fake`。

后端默认地址为 `http://localhost:8080`。健康检查：

```powershell
Invoke-RestMethod http://localhost:8080/api/health
```

### 启动前端

在另一个 PowerShell 窗口执行：

```powershell
cd C:\Users\Administrator\Desktop\newjob\AI\frontend
npm install
npm run build
npm run dev
```

前端默认地址为 `http://localhost:5173`，开发代理会把 `/api` 请求转发到后端 `http://localhost:8080`。打开页面后输入岗位描述和个人经历，提交即可查看 Fake 匹配报告。报告是稳定演示夹具，不会随真实模型变化。

## 本机 MySQL

MySQL 8.4.9 已作为本机开发环境准备好，但当前应用还不会自动连接或执行迁移。建议先检查服务和端口：

```powershell
Get-Service MySQL*
Test-NetConnection 127.0.0.1 -Port 3306
```

后续接入持久化时使用数据库 `jobcoach` 和普通应用账号，例如 `jobcoach_app`。密码只放在本机环境变量或本地配置中，不提交到 Git；应用不应使用 root 账号。

`backend/src/main/resources/db/migration/schema.sql` 当前是需要评审的 MySQL 8 草案，尚未接入 Flyway/Liquibase、Repository 或启动流程。不要因为数据库已经创建，就把 MySQL 持久化标记为完成。

## 环境变量

从根目录 `.env.example` 查看变量名称。真实配置只保留在本机：

```text
AI_PROVIDER
AI_BASE_URL
AI_MODEL
AI_API_KEY
MYSQL_HOST
MYSQL_PORT
MYSQL_DATABASE
MYSQL_USERNAME
MYSQL_PASSWORD
```

第一阶段使用 Fake 模式，不要求配置 AI Key。真实模型实验前必须确认日志不会输出 Authorization header、API Key、岗位隐私或完整个人经历。

### 真实模型模式

可在项目根目录创建 `.env`（已被 Git 忽略），从 `backend` 目录启动时 Spring 会读取它。文件使用 `NAME=value` 格式，不加引号或 `export`；不要提交真实 Key。`.env.example` 只提供字段名，不包含真实配置。`AI_BASE_URL` 应包含供应商的 API 基础路径（例如 `/v1`），应用会追加 `/chat/completions`。当前适配器要求服务兼容 OpenAI Chat Completions 的 `choices[0].message.content` 响应格式。

```text
AI_PROVIDER=real
AI_BASE_URL=https://你的模型服务地址/v1
AI_MODEL=你的准确模型名
AI_API_KEY=你的本机Key
```

然后从 `backend` 目录执行 `mvn spring-boot:run`。系统环境变量优先于 `.env` 同名配置；排查模式时先检查当前 PowerShell 是否设置过 `$env:AI_PROVIDER`。如果不希望 Key 落盘，也可只在启动后端的 PowerShell 窗口设置：

```powershell
cd C:\Users\Administrator\Desktop\newjob\AI\backend
$env:AI_PROVIDER = 'real'
$env:AI_BASE_URL = 'https://你的模型服务地址/v1'
$env:AI_MODEL = '你的准确模型名'
$env:AI_API_KEY = [System.Net.NetworkCredential]::new('', (Read-Host 'AI API Key' -AsSecureString)).Password
mvn spring-boot:run
```

如果服务不要求 Bearer Key，可不设置 `AI_API_KEY`；是否允许无 Key 由供应商决定。不要把真实 Key 写入命令、`.env.example`、截图或聊天。后端启动后，用脱敏输入调用 `POST http://localhost:8080/api/matches`，再启动前端 `npm run dev` 检查页面。PowerShell 环境变量只作用于当前窗口；关闭窗口后需重新设置。切回 Fake 时，停止后端并在同一窗口设置 `$env:AI_PROVIDER = 'fake'` 后重启，或将 `.env` 中的值改为 `fake` 并重启。

若启动时提示缺少 `AI_BASE_URL`，检查基础地址；`AI_CONFIG_INVALID` 表示模型名缺失；`AI_PROVIDER_ERROR` 表示上游请求失败。返回报告前仍需验证结构和内容，不能用服务启动成功代替真实模型验收。

### 真实模式错误与重试演示

浏览器错误/重试可以在不改配置的情况下验证：先提交一次脱敏输入，然后在后端窗口按 Ctrl+C 停止服务；再次点击“分析岗位匹配”应显示网络错误并保留输入，不显示报告。重启后端后点击“重试分析”，应重新发出请求。要单独验证真实上游的 HTTP 502，可临时覆盖本机启动窗口的 `AI_BASE_URL` 为不可连接地址并重启，验证后关闭该窗口再用原 `.env` 启动；不要改写或输出真实地址和 Key。

## MVP 本机验收清单

以下按顺序执行。若 8080 或 5173 已有服务，先确认它是本项目实例，不要再启动第二份。所有输入使用虚构的岗位和经历，不要把真实简历、Key 或模型响应全文放进截图和记录。

1. **自动检查。** 在项目根目录运行下方三条命令。预期 Maven 测试、前端构建和补丁格式检查均退出 0；当前基线是 8 个测试类、22 个用例、0 失败。

   ```powershell
   cd C:\Users\Administrator\Desktop\newjob\AI
   mvn -q -f backend\pom.xml test
   npm --prefix frontend run build
   git diff --check
   ```

2. **启动 Real 模式。** 确认根目录 `.env` 已在本机配置 `AI_PROVIDER=real`、模型地址、模型名和 Key，不要打印文件内容。在两个新的 PowerShell 窗口分别执行：

   ```powershell
   cd C:\Users\Administrator\Desktop\newjob\AI\backend
   mvn spring-boot:run
   ```

   ```powershell
   cd C:\Users\Administrator\Desktop\newjob\AI\frontend
   npm run dev
   ```

   已有本项目服务时直接复用。访问 `http://localhost:8080/api/health` 应成功；浏览器打开 **`http://localhost:5173`**，不要用 `127.0.0.1:5173`，因为当前 CORS 默认只允许 `localhost`。

3. **验证真实报告与预览。** 岗位输入“Java 后端工程师，要求 Spring Boot 和 REST API 测试”，经历输入“做过 Java Web 项目，写过接口集成测试”。点击“分析岗位匹配”，应先看到加载状态，再看到 0-100 分、要求、证据、差距、风险和建议。真实分数和文案不固定。点击“生成计划预览”，应看到 1-5 个任务及优先级、目标、完成标准，并显示“仅供预览，尚未保存”。刷新页面后结果不会保留；不要把预览理解成数据库保存。

4. **验证输入与错误恢复。** 清空任一输入，提交按钮应禁用。恢复输入后按上一节的方法停止后端并重新提交，页面应显示错误、保留输入且不显示旧报告；重启后端并点击“重试分析”，应重新发起请求。用浏览器窄屏模式检查约 390px 宽度下没有横向滚动，长文本仍在输入框和报告区域内正常换行。

5. **验证 Fake 回退。** 停止 Real 后端，在新的 PowerShell 窗口执行：

   ```powershell
   cd C:\Users\Administrator\Desktop\newjob\AI\backend
   $env:AI_PROVIDER = 'fake'
   mvn spring-boot:run
   ```

   刷新页面并提交同一组输入，应得到固定的 72 分报告；点击“生成计划预览”应显示任务。Fake 内容不会随输入变化。结束后关闭此窗口，按第 2 步重新启动 Real 模式。

6. **可选的脱敏业务探测。** Real 后端运行时，在项目根目录执行 `./docs/ai-experiments/test-local-api.ps1`，默认发送 005–007 三个虚构业务案例，每例一次；`-CaseId backend-evidence-005 -Count 3` 可重复单例。脚本只打印案例 ID、HTTP 状态、耗时和结构结果；`structure_ok` 后仍需对照 `docs/ai-experiments/minimal-eval-set.jsonl` 中的 `check` 人工检查证据、差距和建议。偶发 504 需如实记录，不把成功等同于长期稳定。验收记录只写“符合/不符合、状态码、耗时、简短原因”，不记录密钥或完整请求/响应。各测试脚本的用途与边界见 `07-testing-strategy.md`。

完成标准：自动检查通过，Real 和 Fake 均能显示报告与预览，错误/重试与窄屏检查符合预期。产品级 RAG、计划保存和历史查询不属于本轮 MVP。

## Docker 可选方案

只有在决定使用 Compose 管理 MySQL 时，才执行 `docker compose up -d mysql`。如果本机 MySQL 已占用 3306，请先停止本机服务或修改 Compose 端口映射。不要在不确认数据卷的情况下执行 `docker compose down -v`。

## 常见故障

| 现象 | 排查 |
|---|---|
| `mvn` 找不到 | 检查 Maven PATH，使用 `mvn -version` 而不是 `maven -version` |
| 后端 8080 被占用 | 查找占用进程，或临时修改 `server.port` |
| 前端请求失败 | 确认后端已在 8080 启动，检查 Vite 代理配置 |
| MySQL 连接异常 | 同时检查 `Get-Service MySQL*` 和 3306 端口；服务停止但端口可连接时，先确认实际占用 3306 的实例；Fake 模式可暂时不依赖 MySQL |
| 前端构建失败 | 重新执行 `npm install`，再运行 `npm run build` |
| 真实模型失败 | 先确认 endpoint、模型名、Key 和能力类型；不要直接删除 Fake 回归路径 |

## 当前验收命令

```powershell
cd C:\Users\Administrator\Desktop\newjob\AI
git diff --check
mvn -q -f backend\pom.xml test
npm --prefix frontend run build
```
