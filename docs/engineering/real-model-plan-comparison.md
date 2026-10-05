# 真实模型接入方案对比

本阶段对比三种实现路径，选择最小可验证的 Spring `RestClient` OpenAI-compatible 适配器。

| 方案 | 优点 | 风险/成本 | 结论 |
|---|---|---|---|
| LangChain4j | 抽象完整，后续工具调用扩展方便 | 当前 pom 未引入，版本兼容和结构化输出行为需额外验证 | 暂缓 |
| Spring `RestClient` | 无新增运行时依赖，endpoint/model/key 可配置，便于 Mock HTTP 测试 | 需自行解析响应和校验结构 | **本轮采用** |
| 直接 `HttpClient` | JDK 原生 | 超时、JSON、错误映射样板更多 | 不采用 |

结构化输出采用固定 JSON schema 提示词，并在网关边界使用 Jackson 映射与业务校验；模型错误、网络错误、响应非法统一映射为可识别的 AI 错误。Fake gateway 继续由 `fake` profile 提供，真实实现只在 `real` profile 激活，避免本地无 key 时启动失败。

Tool Calling 本轮只保留接口边界，不让模型触发持久化或外部副作用；后续可在独立迭代中接入白名单工具。
