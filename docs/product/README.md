# 产品文档状态与待确认项

## 已由用户确认

- 产品名：AI 求职教练系统（JobCoach）。
- 用户目标权重：简历项目作品 4、AI coding/概念/工具/Codex 4、Java 后端 2。
- 以岗位描述与个人经历分析匹配、差距并制定准备计划为项目方向。

## 当前产品方案（草案，不等于逐字段确认）

- 首个 MVP 输入为岗位描述和简历/项目经历文本。
- 输出岗位要求、经历证据、技能差距、风险/不确定性和准备建议。
- 保存准备计划之前由用户显式触发。
- 第一版不自动投递、不抓取招聘网站、不做复杂权限或多 Agent。

## 文档索引

编号文档是当前产品主规格，按 `00` 到 `09` 阅读：

1. [产品简述](00-product-brief.md)
2. [用户流程](01-user-flows.md)
3. [MVP 范围](02-mvp-scope.md)
4. [功能需求](03-functional-requirements.md)
5. [非功能需求](04-non-functional-requirements.md)
6. [领域模型](05-domain-model.md)
7. [API 契约](06-api-contract.md)
8. [错误模型](07-error-model.md)
9. [演示脚本](08-demo-script.md)
10. [简历描述](09-resume-description.md)
11. [Codex 扩展需求独立提案](13-codex-extension-requirements.md)
12. [Qoder 扩展需求独立提案](14-qoder-extension-requirements.md)
13. [三方交叉评审共识规格](15-consensus-requirements.md)
14. [第 1 期产品基座需求规格（正式版）](16-phase1-requirements.md)

跨文档说明合并到单篇 `overview.md`：

- [产品概览（需求/架构/API）](overview.md)

## 后续只需集中确认的产品问题

1. 匹配报告是否显示单一总分，还是只给逐项匹配状态和证据？建议先逐项展示，避免伪精确。
2. 第一版是否要保存简历/岗位全文？建议默认不长期保存原文，只保存用户主动保存的报告/计划；最终方案需结合演示需求确认。
3. 保存计划前是否要求用户确认？建议要求确认，避免模型直接产生未审阅副作用。
4. 历史分析列表是否属于 P0？建议若时间充足加入，否则先完成单次端到端流程。
