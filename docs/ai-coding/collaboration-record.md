# 协作记录

## 决策

采用 Spring `RestClient` 实现真实模型适配，保留 `AiGateway` 接口和 Fake 回退；本轮不引入 LangChain4j，不执行有副作用的 Tool Calling。真实模型能力以实验结果为准。

## 进度

| 时间块 | 目标 | 状态/证据 |
|---|---|---|
| 第 1 小时 | 上下文和基线 | 完成；`mvn test -q`、`npm run build`、`git diff --check` 通过 |
| 第 2 小时 | 需求评审 | 已完成规格整理；见 `docs/product/03-functional-requirements.md`；真实模型相关验收待实验 |
| 第 3 小时 | 优先级与任务拆分 | 已完成；任务、依赖和回滚点见下 |
| 第 4 小时 | 方案与接口设计 | 已完成文档契约；超时与完整校验仍待代码和测试 |
| 第 5-7 小时 | 模型能力探测 | Qwen3.5 结构化报告单样本成功；普通文本和异常待验证 |
| 第 8-11 小时 | 后端闭环 | 待进行 |
| 第 12-14 小时 | 前端联调 | Fake 闭环已有；用户提供真实模式成功截图，错误与重试待验证 |
| 第 15-17 小时 | Review 与回归 | 待进行 |
| 第 18-20 小时 | 演示和交付 | 待进行 |

## 第 1 小时：上下文和基线

- 目标：建立项目基线，确认冲刺目标是“真实模型结构化匹配、Fake 回退、可展示联调”。
- 完成：盘点请求链路、gateway、前端和测试入口；确认 Fake 与 Real gateway 源码均存在。
- 验证：`mvn test -q`、`npm run build`、`git diff --check` 全部通过；初始工作区干净。
- 发现：真实模型调用、Real profile 启动及超时行为仍待验证；`backend/README.md` 对 gateway 状态的描述过时。
- 下一步：进行需求评审，明确成功/失败验收样例。MySQL、RAG、登录、上传和有副作用的 Tool Calling 暂不纳入本轮。

### 本机验证 Fake 模式

Fake 模式默认启用，不需要 API Key、MySQL 或外网。

PowerShell 窗口一，启动后端并保持运行：

```powershell
cd C:\Users\Administrator\Desktop\newjob\AI\backend
mvn spring-boot:run
```

PowerShell 窗口二，启动前端并保持运行：

```powershell
cd C:\Users\Administrator\Desktop\newjob\AI\frontend
npm run dev
```

浏览器打开 `http://localhost:5173`，输入岗位描述和经历并提交。页面应显示固定 Fake 报告；它用于验证请求和展示链路，内容不会按输入变化。

也可直接验证 API：

```powershell
$body = @{
  jobDescription = "Java 后端工程师"
  profile = "参与过 Java 项目"
} | ConvertTo-Json

Invoke-RestMethod `
  -Uri http://localhost:8080/api/matches `
  -Method Post `
  -ContentType "application/json" `
  -Body $body
```

输入校验可用空岗位描述测试，预期 HTTP 400、错误码 `INPUT_INVALID`。后端测试命令为 `mvn test -q`，前端构建命令为 `npm run build`。

## 第 2 小时：需求评审

- 目标/决定：将 P0 收敛为两段文本输入、结构化报告、可恢复错误和 Fake 演示；用户价值是识别岗位证据与优先补足项。
- 实施或实验：读取产品简述、用户流程、MVP 范围与需求拆解；整理 `docs/product/03-functional-requirements.md` 中的成功、失败、边界和非目标验收。
- 验证结果：规格已对照现有 API、前端字段及测试；真实模型和浏览器演示仍待实际验证。
- 发现与风险：Fake 是固定报告，需切换 profile 使用，不会在真实请求失败后自动降级；无 API Key 时真实模型能力不能宣称通过。
- 下一步：按 P0 拆分后端、前端、测试和文档任务，固定接口边界与回滚点。

后续产品方向已确认：以 AI/Agent 能力为主线。下一增量先生成准备计划预览；用户确认与跨重启保存随后单独实施。功能地图见 `docs/product/03-functional-requirements.md`，任务顺序见 `docs/plan/next-backlog.md`。

## 第 3 小时：优先级与任务拆分

- P0：真实模型结构化报告、输出校验、稳定错误、前端展示和 Fake 模式回归。P1：准备计划预览的需求与实验材料。P2：RAG、模拟面试等后续能力；本轮不实现。
- 顺序：先固定接口和模式配置，再做后端错误/校验与测试，随后前端联调，最后运行 Fake 与真实模式验收并同步文档。真实模型实验依赖用户本机提供环境变量；未配置时保持“待验证”。

| 任务/负责人 | 依赖与交付 | 回滚点 |
|---|---|---|
| 后端 / 后端负责人 | 沿用 `AiGateway`、`MatchReport` 和 `POST /api/matches`；补真实 gateway 的超时、输出校验及错误分类，保持 Fake profile 可用 | 仅撤回 Real gateway 与配置改动，Fake 实现可独立运行 |
| 前端 / 前端负责人 | 依据既有 API 契约检查加载、报告、失败提示和重试；接入后端稳定错误码后完成真实模式联调 | 撤回本轮页面/API client 改动，保留既有 Fake 页面 |
| 测试 / 对应模块负责人 | 后端覆盖输入边界、模型响应异常、超时和 Fake 回归；前端至少通过构建并做成功/错误/重试演示 | 测试随所属模块回滚，不改断言掩盖失败 |
| 文档与验收 / 主负责人 | 记录脱敏模型实验、接口决定、命令结果和已验证状态；更新 README 与状态矩阵 | 文档按证据修正，不把计划标成已完成 |

- Review 发现：原先 `@Profile("real")` 与 `AI_PROVIDER` 不一致；已改为属性选择。错误码和 HTTP 契约见 `docs/product/06-api-contract.md`。
- 下一步：方案与接口设计，核对 `AI_CONFIG_INVALID`、`AI_PROVIDER_ERROR`、`AI_RESPONSE_INVALID` 与产品错误草案的命名和 HTTP 状态。

## 第 4 小时：方案与接口设计

- 决定：沿用 Spring `RestClient`、`AiGateway`、`MatchReport` 和 `POST /api/matches`，不引入新框架或自动 Fake 降级。模式由 `AI_PROVIDER` 选择。
- 契约：输入错误 400；配置错误 500；上游错误 502；非法响应 502；上游超时目标为 504/`AI_TIMEOUT`。错误体保持 `code/message/path/timestamp`，详见 API 契约。
- 验证：Qwen3.5 单样本成功；模拟上游 503、读取超时、非法 JSON、缺字段、嵌套字段错误和分数越界测试通过；前端构建通过。
- 验证：补充了 20,000 字符上限成功和 20,001 字符拒绝测试；相关 Controller 测试、完整 `mvn test -q`、`npm run build`、`git diff --check` 均通过。真实服务 HTTP 状态/耗时和浏览器错误/重试演示仍待本机执行。
- 下一步：按 `docs/ai-experiments/model-experiment.md` 的脱敏脚本记录真实状态/耗时，再按本机运行说明制造连接失败并验证页面重试；保留 Fake 回归。

## 目标校正（待执行）

用户指出原 20 小时表对概念和 Codex 实操缺少可验收安排。相关收口项现已转入 `docs/plan/mvp-closeout-plan.md`，覆盖 Prompt、Tool Calling、Workflow/Agent、RAG、Skill、MCP、Plan、Thread、Review 和测试。第 1-3 小时只按上方实际记录认定完成；新增练习须执行后再标记通过。

## 真实模式配置修正

用户决定优先接入真实模型。已将 gateway 选择统一到 `AI_PROVIDER=fake|real`，支持从项目根目录的 Git 忽略 `.env` 读取本机配置，并补真实模式选择测试。首次相关测试因 Spring Boot 4 未提供旧版 Jackson `ObjectMapper` Bean 而失败；在配置类显式注册后，相关测试和 `mvn test -q` 均通过。临时无 Key `.env` 仅设置 `AI_PROVIDER=real` 后，启动出现预期 `AI_BASE_URL is required`，证明导入生效；测试文件已移除。真实服务请求仍待本机配置 endpoint、模型名和 Key 后验证。

## 真实模型成功样例（用户提供）

用户确认本机已接入 Qwen3.5，并提供一次页面截图：80 分报告成功显示要求、匹配证据、技能差距、风险和建议。此前另有一次约 5.2 秒的 HTTP 200 和一次 17299 ms 的 HTTP 504。2026-10-06 新增可复用脱敏 API 脚本，三次请求为 HTTP 200/504/200，耗时 15130/17074/16202 ms；17 秒读取超时映射已由真实请求验证。临时将上游地址覆盖为不可连接的本机端口，浏览器首次提交和重试均返回 502；错误显示、输入保留和无伪报告已验证，随后恢复真实后端。恢复后的请求返回 504/19063 ms。当前多次稳定性、普通文本独立探测和非法结构真实输出仍待验证。未记录 Key 或完整响应。

## 2026-10-07 MVP 收口执行

- 阶段 4：真实模式从 `backend` 启动，三次脱敏匹配在原 17 秒阈值下为 200/504/504；将默认后端读取超时调为 25 秒、前端调为 30 秒后，三次均为 HTTP 200。普通文本独立请求 HTTP 200；真实只读 Tool Calling 预览 HTTP 200、5 任务、无保存 ID。详见 `docs/ai-experiments/model-experiment.md`。
- 阶段 5：`mvn test -q` 通过，8 类 22 例，0 失败；越权工具、额外动作参数、非法优先级、空响应、Fake 预览均有测试。
- 阶段 6：Tabbit 浏览器验证 Real 报告和预览、Fake 72 分报告和预览、390px 无横向溢出、5000 字输入无溢出；受控 502 下输入保留、无伪报告、重试发出第二次请求。截图工具超时，故本轮证据为 DOM/布局检查，不宣称取得截图。
- 阶段 7-8：Review 发现空模型正文错误分类不准确、重新生成失败保留旧预览、Fake 无技能差距时预览为空，均已最小修复；`npm run build`、`git diff --check` 通过。
- 阶段 9：已使用 `planning-with-files`、`tabbit` 和项目级 `jobcoach-closeout-check`，MCP 只读获取验证运行时；完成 RAG 概念对照，但产品 RAG 仍暂缓。`codex review --uncommitted` 未发现需修复的功能性缺陷；Review 进程受沙箱限制无法有效复跑 Maven，且误报前端缺少 package 配置。长期模型稳定性与真实自然非法结构仍待验证。

## 每小时记录

后续每小时只补充以下信息，保留可复查证据：

```text
目标/决定：
实施或实验：
验证结果：
发现与风险：
下一步：
```
