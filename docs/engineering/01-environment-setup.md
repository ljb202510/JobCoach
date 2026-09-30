# 环境准备与安装验收

## 已确认版本

Java OpenJDK 21.0.12.1 LTS；Maven 3.9.10（命令是 `mvn`）；Git 2.55.0.windows.3；Node.js 20.18.2；npm 10.8.2；pnpm 9.15.9。MySQL 8.4.9 已由用户安装并创建 `jobcoach` 数据库；Docker Desktop 仍是可选方案。

## 复查命令

`java -version`、`mvn -version`、`git --version`、`node --version`、`npm --version`、`pnpm --version`、`docker --version`、`docker compose version`。

## Docker Desktop

1. 运行 `winver`，建议 Windows 10 22H2 或 Windows 11。
2. 管理员 PowerShell 执行 `wsl --install`，按提示重启。
3. 重启后执行 `wsl --status` 和 `wsl --list --verbose`，确认 WSL 2。
4. 从 [Docker 官方文档](https://docs.docker.com/desktop/setup/install/windows-install/) 下载 Docker Desktop。
5. 安装时启用 WSL 2 backend，保持 Linux containers。
6. 启动后执行 `docker --version`、`docker compose version`、`docker run --rm hello-world`。

安装可能需要管理员权限、重启和 BIOS 虚拟化支持，需要用户在本机确认。

## MySQL

当前默认使用本机 MySQL 8.4.9。Docker Compose 只作为可选替代方案，不能与本机 MySQL 默认同时占用 3306。不要随意执行会删除数据卷的 `down -v`。

数据库名为 `jobcoach`。后续应用接入时使用普通用户 `jobcoach_app`，示例密码仅用于本地占位，实际值应通过本地环境配置且不得提交。数据库已创建不代表 MySQL 持久化功能已完成；schema 仍需迁移和集成测试。

## 模型变量

不要提交真实密钥。提交 `.env.example`，本地使用 `AI_PROVIDER`、`AI_BASE_URL`、`AI_MODEL`、`AI_API_KEY`。先测文本，再测结构化输出、工具调用、流式和 Embedding。
