export class ApiRequestError extends Error {
    payload;
    constructor(payload) {
        super(payload.message);
        this.payload = payload;
    }
}
export async function analyzeMatch(request) {
    let response;
    const controller = new AbortController();
    const timeout = window.setTimeout(() => controller.abort(), 20_000);
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
            throw new Error('分析超过 20 秒仍未完成，请检查模型服务后重试。');
        }
        throw new Error('网络请求失败，请确认后端服务已启动。');
    }
    finally {
        window.clearTimeout(timeout);
    }
    if (!response.ok) {
        const payload = await response.json().catch(() => ({
            code: 'HTTP_ERROR',
            message: '服务返回了无法识别的错误',
            path: '/api/matches',
            timestamp: new Date().toISOString(),
        }));
        throw new ApiRequestError(payload);
    }
    return (await response.json());
}
