param(
  [ValidateSet('text', 'structured', 'tool')]
  [string] $Mode = 'text'
)

if (-not $env:AI_BASE_URL -or -not $env:AI_MODEL -or -not $env:AI_API_KEY) {
  throw '请通过环境变量设置 AI_BASE_URL、AI_MODEL、AI_API_KEY；不要把密钥写入脚本。'
}

$body = switch ($Mode) {
  'text' { @{ model = $env:AI_MODEL; messages = @(@{ role = 'user'; content = '用一句话说明岗位匹配分析的目标' }) } }
  'structured' { @{ model = $env:AI_MODEL; messages = @(@{ role = 'user'; content = '只输出 JSON：{"matchScore":0,"evidence":[]}' }); response_format = @{ type = 'json_object' } } }
  'tool' { @{ model = $env:AI_MODEL; messages = @(@{ role = 'user'; content = '为 Spring Boot 制定一个准备任务' }); tools = @(@{ type = 'function'; function = @{ name = 'draft_preparation_task'; description = '仅返回待确认的任务草案'; parameters = @{ type = 'object'; properties = @{ title = @{ type = 'string' }; reason = @{ type = 'string' } }; required = @('title', 'reason') } } }) } }
}

$response = Invoke-RestMethod -Uri "$($env:AI_BASE_URL.TrimEnd('/'))/chat/completions" -Method Post `
  -Headers @{ Authorization = "Bearer $($env:AI_API_KEY)" } -ContentType 'application/json' `
  -Body ($body | ConvertTo-Json -Depth 10)

[ordered]@{ mode = $Mode; model = $env:AI_MODEL; response = $response } | ConvertTo-Json -Depth 10
