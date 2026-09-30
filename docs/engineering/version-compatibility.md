# 版本兼容性记录

## 已确认环境

- Java OpenJDK 21.0.12.1 LTS
- Maven 3.9.10
- Node.js 20.18.2
- npm 10.8.2
- pnpm 9.15.9

## 当前基线

- Spring Boot 4.1.1 skeleton 已通过 `mvn test -q`。
- LangChain4j、MyBatis-Plus 和 MySQL 依赖尚未加入最终构建，原因是需要分别核验版本和集成方式；这不是模型服务不可用的判断。

## 核验规则

1. 先读项目官方文档/发布说明；2. 确认 Java 和 Spring Boot 支持范围；3. 用最小示例编译；4. 记录冲突和替代；5. 再进入业务代码。

这份记录只写已验证事实，候选版本放在“待验证”中，不能当作最终选型。

## 官方来源入口

- Spring Boot System Requirements: https://docs.spring.io/spring-boot/system-requirements.html
- Spring Initializr metadata: https://start.spring.io/metadata/client
- LangChain4j docs: https://docs.langchain4j.dev/intro
- MyBatis-Plus docs: https://baomidou.com/
- MySQL Connector/J docs: https://dev.mysql.com/doc/connector-j/en/
