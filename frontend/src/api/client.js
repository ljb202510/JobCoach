export class ApiRequestError extends Error {
    details;
    constructor(code, status, operation) {
        super(errorMessage(code, status, operation));
        this.details = `HTTP ${status} · ${code}`;
    }
}
export class ApiNetworkError extends Error {
}
const knownCodes = new Set([
    'INPUT_INVALID', 'BODY_INVALID', 'AI_CONFIG_INVALID', 'AI_PROVIDER_ERROR',
    'AI_TIMEOUT', 'AI_RESPONSE_INVALID', 'INTERNAL_ERROR',
]);
function errorMessage(code, status, operation) {
    switch (code) {
        case 'INPUT_INVALID':
        case 'BODY_INVALID':
            return operation === 'plan'
                ? '当前报告无法用于生成计划。请重新分析岗位，确认报告中包含技能差距或建议后再试。'
                : '输入内容不符合要求。请检查岗位描述和个人经历是否填写完整、长度是否过长，然后重新提交。';
        case 'AI_CONFIG_INVALID':
            return '模型配置不完整。请检查后端的模型名称和连接配置，修正后重试。';
        case 'AI_PROVIDER_ERROR':
            return '模型服务请求失败，可能是连接、认证或上游服务异常。请检查后端模型配置与服务状态，稍后重试。';
        case 'AI_TIMEOUT':
            return '模型响应超时，本次分析未完成。请稍后重试；若反复出现，请检查模型服务响应速度。';
        case 'AI_RESPONSE_INVALID':
            return '模型返回的报告格式不符合要求，本次结果未采用。请重试；若持续出现，可更换模型或检查其结构化输出能力。';
        case 'INTERNAL_ERROR':
            return '后端处理请求时出错。请稍后重试；若持续出现，请查看后端日志。';
    }
    if (status === 404)
        return '服务接口不存在。请确认前端与后端运行的是同一版本。';
    if (status === 502 || status === 503 || status === 504) {
        return '服务暂时无法完成请求。请稍后重试；本机运行时请检查后端和模型服务状态。';
    }
    if (status >= 500)
        return '服务处理请求时出错。请稍后重试；若持续出现，请查看后端日志。';
    return '请求未能完成。请检查输入或刷新页面后重试。';
}
async function readRequestError(response, operation) {
    const payload = await response.json().catch(() => null);
    const code = payload && typeof payload.code === 'string' && knownCodes.has(payload.code)
        ? payload.code : 'HTTP_ERROR';
    return new ApiRequestError(code, response.status, operation);
}
async function readSuccess(response) {
    try {
        return await response.json();
    }
    catch {
        throw new ApiNetworkError('服务返回的数据格式异常。请重试；若持续出现，请检查后端日志。');
    }
}
export async function analyzeMatch(request) {
    let response;
    const controller = new AbortController();
    const timeout = window.setTimeout(() => controller.abort(), 30_000);
    try {
        response = await fetch('/api/matches', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(request),
            signal: controller.signal,
        });
    }
    catch (error) {
        if (error instanceof DOMException && error.name === 'AbortError') {
            throw new ApiNetworkError('分析超过 30 秒仍未完成，请检查模型服务后重试。');
        }
        throw new ApiNetworkError('无法连接到服务。请检查网络连接；本机运行时请确认后端已启动。');
    }
    finally {
        window.clearTimeout(timeout);
    }
    if (!response.ok) {
        throw await readRequestError(response, 'match');
    }
    return readSuccess(response);
}
export async function previewPreparationPlan(report) {
    const controller = new AbortController();
    const timeout = window.setTimeout(() => controller.abort(), 30_000);
    let response;
    try {
        response = await fetch('/api/preparation-plans/preview', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(report),
            signal: controller.signal,
        });
    }
    catch (error) {
        if (error instanceof DOMException && error.name === 'AbortError') {
            throw new ApiNetworkError('准备计划超过 30 秒仍未完成，请重试。');
        }
        throw new ApiNetworkError('无法连接到服务。请检查网络连接；本机运行时请确认后端已启动。');
    }
    finally {
        window.clearTimeout(timeout);
    }
    if (!response.ok) {
        throw await readRequestError(response, 'plan');
    }
    return readSuccess(response);
}
