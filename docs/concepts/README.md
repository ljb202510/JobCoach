# 核心概念学习包

本目录服务于 4:4:2 目标中的 AI 概念和工具学习。阅读时结合 JobCoach 代码验证，不要求死记术语。

每篇使用相同学习路径：定义 → 为什么存在 → 最小示例 → 相邻概念边界 → JobCoach 映射 → 验证方法 → 失败与局限 → 面试表达。

## 阅读顺序

1. [AI 应用全景](01-ai-application-overview.md)
2. [Prompt 与上下文](02-prompt-and-context.md)
3. [结构化输出](03-structured-output.md)
4. [Tool Calling](04-tool-calling.md)
5. [Agent loop](05-agent-loop.md)
6. [Workflow 与 Agent](06-workflow-vs-agent.md)
7. [RAG](07-rag.md)
8. [Memory 与状态](08-memory-and-state.md)
9. [模型能力与兼容性](09-model-capabilities.md)
10. [AI 应用测试](10-ai-application-testing.md)
11. [Spring AI 与 LangChain4j](11-spring-ai-vs-langchain4j.md)
12. [Java AI 应用架构](12-java-ai-architecture.md)

每篇中的“JobCoach 实验”需要在实际开发后补充运行证据；概念说明本身不代表能力已经验证。

## 每篇文档的学习方法

不要连续通读 12 篇后再动手。每篇按下面顺序完成一个小循环：

1. 先用自己的话写定义，并列出一个相邻但不同的概念。
2. 找到 JobCoach 中对应的文件、接口或尚未实现的边界，不能只停留在抽象术语。
3. 运行一个最小实验：可以是单元测试、fake gateway、只读脚本、能力探测或手工 HTTP 请求。
4. 记录输入、命令、结果、失败原因和没有覆盖的范围；真实模型实验必须记录模型/Prompt/数据版本。
5. 用三分钟口头解释“它解决什么问题、为什么不能滥用、如果失败如何降级”。
6. 只有当实验或源码证据支持时，才把“待验证”改成“已验证”。

## 概念之间的依赖关系

```text
AI 应用全景
  ├─ Prompt 与上下文 -> 结构化输出 -> 测试
  ├─ Tool Calling -> Agent loop -> Workflow/Agent 取舍
  ├─ RAG -> Memory/State -> 隐私与恢复
  ├─ 模型能力探测 -> Spring AI/LangChain4j 选型
  └─ Java AI 架构把上述能力放入可测试边界
```

## 完成标准

完成一篇不等于背完术语。至少应留下：一张边界图、一个最小实验、一个失败/局限记录、一个 JobCoach 映射和两个面试追问答案。实验未执行时，在文档中保留“未验证”，不要用概念理解冒充工程能力。
