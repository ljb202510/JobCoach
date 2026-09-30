import type { ApiError, MatchReport, MatchRequest } from './types'

export class ApiRequestError extends Error {
  constructor(public readonly payload: ApiError) {
    super(payload.message)
  }
}

export async function analyzeMatch(request: MatchRequest): Promise<MatchReport> {
  let response: Response
  try {
    response = await fetch('/api/matches', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(request),
    })
  } catch {
    throw new Error('网络请求失败')
  }
  if (!response.ok) {
    const payload = await response.json().catch(() => ({
      code: 'HTTP_ERROR',
      message: '服务返回了无法识别的错误',
      path: '/api/matches',
      timestamp: new Date().toISOString(),
    })) as ApiError
    throw new ApiRequestError(payload)
  }
  return (await response.json()) as MatchReport
}
