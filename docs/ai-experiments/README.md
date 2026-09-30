# AI 实验与评测材料

本目录只准备可复现的请求样例、夹具和评测模板，不包含密钥，也不宣称任何渠道已经支持某项能力。

- `request-samples.http`：普通文本、结构化 JSON、Tool Calling 的请求样例。
- `fixtures.json`：正常、空输出、非法 JSON、缺字段和工具参数异常样例。
- `minimal-eval-set.jsonl`：JobCoach 最小评测集，记录期望关注点而非模型分数。
- `run-experiment.ps1`：从环境变量读取 endpoint/model/key 的脱敏实验脚本。
- `model-record.md`：按模型/渠道记录真实实验结果。
- `prompt-version-template.md`：Prompt 版本和变更原因模板。
- `quality-checklist.md`：结构、证据、风险和安全检查清单。

执行实验前设置 `AI_BASE_URL`、`AI_MODEL` 和 `AI_API_KEY`；脚本不会把 key 写入文件或输出。
