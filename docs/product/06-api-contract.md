# API 契约

## 生成匹配报告

`POST /api/matches`

请求头为 `Content-Type: application/json`；`jobDescription` 与 `profile` 都是必填的非空字符串，各自最长 20,000 字符。成功时返回 HTTP 200。

```json
{
  "jobDescription": "Java 后端工程师，要求 Spring Boot 和 AI 应用经验",
  "profile": "软件工程大四，做过 Java Web 项目"
}
```

成功响应直接返回 `MatchReport`。后续接入持久化后再考虑包装 `executionId` 和状态。

成功响应示例：

```json
{
  "matchScore": 72,
  "requirements": [{"name": "Java", "importance": "HIGH"}],
  "evidence": [{"requirement": "Java", "evidence": "个人经历中包含 Java 后端项目", "matched": true}],
  "skillGaps": [{"skill": "Spring Boot", "reason": "缺少可验证的项目细节", "priority": "HIGH"}],
  "risks": ["当前报告由 fake gateway 生成，仅用于业务链路测试"],
  "recommendations": ["补充 Spring Boot 项目的职责、接口和测试证据"]
}
```

字段校验或 JSON 解析失败均返回 `400`，但错误码区分来源：

```json
{"code":"INPUT_INVALID","message":"请求字段不符合要求","path":"/api/matches","timestamp":"2026-09-30T00:00:00Z"}
```

非法 JSON 请求体返回 `BODY_INVALID`；空字段、超长字段和其他业务输入校验失败返回 `INPUT_INVALID`。

## 本轮失败契约

错误体沿用 `code`、`message`、`path`、`timestamp`，不返回模型原文、API Key、Authorization 或上游敏感头。输入保留在前端，失败不展示伪成功报告。`AI_PROVIDER=fake|real` 选择 gateway；真实请求失败不会自动切到 Fake。

| 场景 | HTTP | `code` | 用户动作 | 当前实现 |
|---|---:|---|---|---|
| 空白/超长字段 | 400 | `INPUT_INVALID` | 修改输入 | 已实现并有空白、20,000 字符边界测试 |
| 非法请求 JSON | 400 | `BODY_INVALID` | 修正请求 | 已测试 |
| 真实模式模型名缺失 | 500 | `AI_CONFIG_INVALID` | 检查本机配置 | 已实现；`AI_BASE_URL` 缺失目前在启动时直接失败 |
| 上游连接、认证或 4xx/5xx 错误 | 502 | `AI_PROVIDER_ERROR` | 检查配置或重试 | 断开上游时真实浏览器请求返回 502；认证和上游 4xx/5xx 分类待补 |
| 上游请求超时 | 504 | `AI_TIMEOUT` | 保留输入后重试 | 真实 Qwen 请求两次返回 504，耗时 17299、17074 ms；17 秒读取超时映射已验证 |
| 模型响应不可解析或报告字段无效 | 502 | `AI_RESPONSE_INVALID` | 重试或切换 Fake 演示 | 顶层和嵌套字段校验已实现并有单测 |

浏览器 30 秒超时属于前端主动中止请求，未必收到后端 HTTP 504；应单独显示超时提示并保留输入。后端默认读取超时为 25 秒，可通过 `AI_READ_TIMEOUT` 覆盖。

真实服务已有 HTTP 200 成功和 HTTP 504 超时证据；三次脱敏请求为 200/504/200，耗时 15130/17074/16202 ms，恢复后另一次请求为 504/19063 ms。多次稳定性仍待验证。浏览器在断开上游时已验证错误、输入保留、无伪报告和重试二次请求。输入长度边界已有自动化测试。

## 准备计划预览

`POST /api/preparation-plans/preview` 接收当前 `MatchReport` JSON，返回 `PreparationPlan`：`status` 固定为 `PREVIEW`，`id` 和 `matchId` 均为 `null`，`tasks` 为 1 到 5 个包含 `title`、`objective`、`priority`、`completionCriteria` 的任务。请求头为 `Content-Type: application/json`，成功时返回 HTTP 200。可直接把上一接口的成功响应作为请求体，例如：

```json
{"matchScore":72,"requirements":[],"evidence":[],"skillGaps":[{"skill":"Spring Boot","reason":"缺少项目证据","priority":"HIGH"}],"risks":[],"recommendations":["补充接口测试经历"]}
```

Fake 模式成功响应示例：

```json
{"id":null,"matchId":null,"status":"PREVIEW","tasks":[{"title":"补充 Spring Boot 的项目证据","objective":"缺少项目证据","priority":"HIGH","completionCriteria":"写出一段包含职责、实现和验证结果的项目经历"}]}
```

该接口不保存计划，也不执行外部动作。`skillGaps` 与 `recommendations` 必须存在且至少一个非空；两者各最多 10 项，报告文本总长度最多 10,000 字符。输入缺失、过长或没有差距与建议时返回 400/`INPUT_INVALID`；真实模型未按白名单调用 `preview_preparation_plan` 或返回非法参数时返回 502/`AI_RESPONSE_INVALID`。Fake 模式优先从差距生成固定规则预览，没有差距时从建议生成。

## 后续候选接口

- `POST /api/matches/{id}/preparation-plan`：用户确认后保存计划。
- `GET /api/matches/{id}`：查看报告和执行记录。
- `PATCH /api/preparation-plans/{id}/tasks/{taskId}`：更新任务状态。

## 契约实施顺序

1. 先锁定 `POST /api/matches` 的请求校验、Fake 成功响应和统一错误。
2. 为模型失败、超时、非法结构和重复提交补 HTTP 测试与前端错误映射。
3. 用户确认保存计划后，再新增候选接口；接口必须携带来源报告、幂等键或等价防重复机制。
4. 只有出现持久化历史和异步执行需求时，才引入 execution ID、查询接口和状态轮询。
5. 每次契约变更同步 API client 类型、演示脚本、错误模型和实现边界。

## 契约不变量

- 成功响应不能包含未经过领域校验的模型字段。
- 错误响应稳定包含 `code`、`message`、必要的 `path` 和时间；不包含密钥和上游敏感头。
- 可重试错误要保留用户输入；不可重试/需确认错误要告诉用户下一步。
- 需要副作用的接口必须有明确用户动作，不能由页面加载或模型自动触发。
