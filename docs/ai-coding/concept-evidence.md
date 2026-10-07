# MVP AI 概念与 Codex 使用证据

| 能力 | 本轮证据 | 边界 |
|---|---|---|
| Prompt | `RealAiGateway` 的岗位匹配 system prompt；真实报告浏览器显示 85 分 | 报告质量未系统评测 |
| 结构化输出 | `RealAiGatewayTest` 覆盖缺字段、越界和空响应；三次真实匹配 HTTP 200 | 真实模型自然非法结构未观察到 |
| Tool Calling | `RealPreparationPlanTool` 只声明 `preview_preparation_plan`；真实调用 HTTP 200/4415 ms，5 任务无 ID | 不保存、不执行外部动作 |
| Workflow/Agent | `MatchAnalysisService` 固定调用 gateway；预览需用户点击，未加入自主 Agent loop | 产品级多 Agent 暂缓 |
| Skill | 已安装 `planning-with-files` 跟踪阶段，`tabbit` 做浏览器验证；项目级 `jobcoach-closeout-check` 已验证并按其门禁试运行 | 项目 Skill 的复用价值需后续增量继续观察 |
| MCP | Codex App 的 `load_workspace_dependencies` 只读获取附带 Python 路径，实际用于项目 Skill 校验 | 浏览器验证由 Tabbit Skill 完成，不把它冒称为 MCP |
| RAG | 2026-10-07 只读对照：不提供仓库片段时模型未给出当前 25 秒值；提供检索到的 `application.yml:17` 后，模型答出 25 秒并引用来源，两次均 HTTP 200 | 只证明流程概念；未接入 JobCoach 产品 RAG，也未评估检索召回率 |

Codex 五项过程能力：本线程是 Thread 证据；`.planning/2026-10-07-mvp-closeout/` 是 Plan 与任务跟踪证据；`codex review --uncommitted` 是独立 Review 证据；`mvn test -q` 的 8 类 22 例全绿是测试证据。Review 未发现需修复的功能性缺陷；其自身的 Maven 复跑被沙箱权限挡住，且误报缺少前端 package 配置，故以本线程的测试与 `frontend/package.json` 为准。此前手工 Review 发现并修复了空模型正文错误分类及重新生成失败时残留旧预览的问题。跨线程协作未在本轮执行。

Skill 选择与试运行：`planning-with-files` 适合本轮多阶段收口，产出 `.planning/2026-10-07-mvp-closeout/` 的计划、发现和进度；`tabbit` 适合浏览器真实联调，验证了 Real/Fake、390px、长文本和受控 502 重试。按 `skill-creator` 规范新增项目级 `jobcoach-closeout-check`，用其三条门禁完成本轮回归，校验器返回 `Skill is valid!`。MCP 只读调用 `load_workspace_dependencies` 找到附带 Python 运行时，再用于项目 Skill 校验。前两项改善了过程追踪和浏览器证据，项目 Skill 是否持续有用留待下一增量复核。
