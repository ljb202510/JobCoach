export class ApiRequestError extends Error {
    payload;
    constructor(payload) {
        super(payload.message);
        this.payload = payload;
    }
}
export async function analyzeMatch(request) {
    let response;
    try {
        response = await fetch('/api/matches', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(request),
        });
    }
    catch {
        throw new Error('网络请求失败');
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
