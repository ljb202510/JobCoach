# Spring AI 与 LangChain4j

## 定位

Spring AI 面向 Spring 应用集成 AI 模型与向量存储等能力；LangChain4j 是 Java AI 应用库，提供模型、AI Service、Tool、Memory、RAG 等抽象。它们有重叠，但抽象和集成方式不同。

## 选型思路

- Spring Boot 企业集成和 Spring 生态优先：评估 Spring AI。
- Java AI Service、Tool、Memory、RAG 编排学习：评估 LangChain4j。
- 先核对所选版本的 Java/Spring Boot 兼容性和 provider 支持，不根据“最新版”推断兼容。

## JobCoach 决策

当前计划优先 LangChain4j，并通过 `AiGateway` 隔离。是否保留该决定要依据官方兼容信息、真实模型实测和项目复杂度再确认。

本文件是学习导读，不代替版本选择时的官方文档核验。

## 对比实验步骤

1. 先用不依赖框架的 `AiGateway` 写清领域接口和测试替身，再比较框架适配成本。
2. 用同一个最小用例分别实现文本调用、结构化输出和 Tool Calling；记录依赖数量、配置方式、异常处理和测试难度。
3. 核对 Java、Spring Boot、starter 和 provider 版本的官方兼容矩阵，不用教程发布日期代替版本证据。
4. 比较框架抽象是否泄漏到 Domain、调试日志是否足够、升级是否容易回滚。
5. 以 JobCoach 的 P0 需求为标准选择，不为展示两个框架而同时引入两个运行时依赖。

## 选择记录模板

| 维度 | 观察问题 | 当前结论 |
|---|---|---|
| 兼容性 | Java 21/Spring Boot 版本是否有官方支持？ | 待核验 |
| 能力 | 文本、结构化、Tool、流式、Embedding 是否分别可用？ | 逐项实测 |
| 测试 | 能否替换 fake gateway 并隔离 provider？ | 必须满足 |
| 运维 | 超时、限流、错误和指标是否可观察？ | 待设计 |
| 迁移 | 更换 provider 或框架的边界在哪里？ | 保留 adapter |

## 复盘问题

1. 这个项目需要的是框架能力，还是一个稳定的 provider adapter？
2. 如果框架升级破坏 starter，哪一层应该吸收变化？
3. 选择 LangChain4j 的学习价值如何转化成可测试的 JobCoach 功能？
