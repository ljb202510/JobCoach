# Frontend

Vue 3 + TypeScript + Vite 前端骨架。页面已覆盖岗位描述和个人经历输入，API 类型与客户端位于 `src/api/`。

当前状态：Fake 模式已打通 `POST /api/matches`，页面包含提交、加载、错误、空状态和结构化匹配报告展示。真实模型、持久化和准备任务仍待后续阶段接入。

## 命令

```powershell
npm install
npm run build
npm run dev
```

开发服务器默认运行在 `http://localhost:5173`，`/api` 请求代理到后端 `http://localhost:8080`。
