# 本地开发与运行说明

本文用于在 Windows PowerShell 中检查环境、运行测试和演示当前 Fake 模式 MVP。它不代表真实模型或 MySQL 持久化已经接入。

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
mvn spring-boot:run
```

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
