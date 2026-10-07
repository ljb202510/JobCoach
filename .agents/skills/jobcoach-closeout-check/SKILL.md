---
name: jobcoach-closeout-check
description: 在将 JobCoach MVP 变更标记为收口完成前，对照 API 契约、Fake/Real 模式、浏览器状态和证据记录进行验收；也适用于后续增量的发布检查。
---

# JobCoach 收口检查

用于 JobCoach MVP 验收或后续增量的发布检查。

1. 阅读 `docs/plan/mvp-closeout-plan.md` 和 `docs/product/06-api-contract.md` 中与本次行为变化相关的契约，只定位受影响的清单项。
2. 在 `backend` 运行 `mvn test -q`，在 `frontend` 运行 `npm run build`，在仓库根目录运行 `git diff --check`。记录退出码，不用旧记录推断本次通过。
3. 验证模型行为时使用固定的脱敏输入，只记录 HTTP 状态、耗时、结果类别和结构事实。不要输出 `.env` 值、Authorization、完整响应体或私人简历与岗位文本。相关场景已有证据后停止重复真实调用。
4. 分别检查 Fake 和 Real 路径。模拟的浏览器失败只证明页面恢复能力，不能证明真实模型服务失败时的行为；每项结论都注明对应证据。
5. 检查差异中的契约、错误映射、副作用和文档变化。未验证的项目保持未完成，并写明下一步具体如何验证。
