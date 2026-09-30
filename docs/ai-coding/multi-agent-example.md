# 多 Agent 委派示例

## 主任务

目标：为 `POST /api/matches` 补齐 Fake 模式端到端链路。

主 Agent 负责 API 契约、集成和最终测试；子任务必须使用独立文件所有权。

## 委派卡 A：后端测试

```text
权威上下文：docs/plan/PROJECT_GOALS.md、docs/product/06-api-contract.md
允许修改：backend/src/test/**
禁止修改：前端、API 响应结构、依赖版本
验收：mvn test -q；回报 HTTP 断言、失败证据和测试缺口
```

## 委派卡 B：文档/评测资产

```text
权威上下文：docs/plan/PROJECT_GOALS.md
允许修改：docs/ai-experiments/**
禁止修改：Java 源码、真实密钥、未经执行的模型结论
验收：样例可读、脚本只从环境变量取密钥、明确未执行状态
```

## 集成检查

主 Agent 合并前检查共享契约、`git diff`、测试退出码和是否出现跨范围改动。多个 Agent 的一致意见不替代源码和命令证据。
