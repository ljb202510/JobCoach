# 项目状态矩阵

这份文件区分“已完成事实”“已确认但未在本项目复现”“方案草案”和“明确暂缓”，避免多个 Agent 把计划当现状。

| 范围 | 状态 | 证据/说明 |
|---|---|---|
| Java/Maven/Node/Git 环境 | 已完成 | `01-environment-setup.md` 中有版本记录 |
| Spring Boot 4.1.1 最小后端 | 已完成 | `backend` 的 `mvn test -q` 退出码 0 |
| fake 岗位匹配 API | 已完成 | `POST /api/matches` 与测试 |
| JobCoach 目标与 4:4:2 权重 | 已确认 | `plan/PROJECT_GOALS.md` |
| 学校 Qwen 模型可调用 | 用户已有外部项目经验确认 | 本项目尚未记录真实请求证据 |
| 本项目普通文本模型调用 | 待验证 | 下一步使用本机环境变量测试 |
| 结构化输出 | 待验证 | 需按模型实际响应测试 |
| Tool Calling | 待验证 | 需按渠道/模型逐项测试 |
| LangChain4j 集成 | 待核验 | 先查版本和最小编译示例 |
| Vue 页面 Fake 闭环 | 已完成 | `npm run build` 通过；提交、加载、错误、空状态和报告展示已接入 |
| MySQL 8.4.9 本机环境 | 环境已具备 | 用户已安装并创建 `jobcoach`；复查时 `MySQL84` 服务为 stopped、3306 可连接，接入前需确认实际实例 |
| MySQL 持久化 | 后续 30 天路线 | 五天 MVP 明确不持久化；schema 是草案，尚未接入迁移、Repository、事务和集成测试 |
| RAG | 暂缓 | 主线稳定前不加入向量数据库 |
| 多 Agent | 文档模板 | 五天 MVP 不实现产品级多 Agent；保留委派和集成示例 |

| 后端分层骨架 | 已完成 | web/application/domain/ai/tool/persistence/config 目录和接口已生成 |
| 前端 Fake API 闭环 | 已完成 | 页面已调用 `POST /api/matches`；加载、错误、空状态和报告展示已接入，`npm run build` 通过 |

五天 MVP 只保留当前请求内的 Fake 状态，服务重启后不保留历史；MySQL、报告/计划保存和历史查询进入 `30-day-incremental-roadmap.md`。

## 文档资产状态

| 资产 | 状态 | 说明 |
|---|---|---|
| 概念文档 | 已生成初稿 | 面向入门和 JobCoach 映射；开发时补实际实验和官方出处 |
| AI coding 手册 | 已生成初稿 | 后续结合真实 Codex 会话迭代 |
| 产品规格 | 草案 | 产品方向确认，字段和隐私细节待集中确认 |
| 版本验证 | 部分完成 | Spring Boot skeleton 已测；其余依赖待核验 |
| 模型能力计划 | 已生成 | 本项目实测待执行；学校服务已有用户外部项目成功经验 |
| 多 Agent 模板 | 已生成 | 需在真实并行任务中验证 |

## 更新规则

每完成一次真实实验或构建验证，就更新对应行的状态和命令证据；没有运行就不能写成“已支持”。
