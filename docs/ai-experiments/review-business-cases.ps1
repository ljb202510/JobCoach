param(
  [string] $Url = 'http://localhost:8080/api/matches'
)

$expectations = @{
  'backend-evidence-005' = @{ matched = @('Spring Boot', 'MySQL|索引'); gap = @('故障|排障|值班') }
  'career-switch-006' = @{ matched = @('Python|向量|检索'); gap = @('Java|Spring AI', '监控|部署|生产') }
  'frontend-gap-007' = @{ matched = @('Vue'); gap = @('Spring Boot', 'MySQL|数据库', '接口测试') }
  'business-frontend-008' = @{ matched = @('React', 'TypeScript'); gap = @('无障碍') }
  'business-backend-go-009' = @{ matched = @('Go', 'Redis'); gap = @('gRPC', '监控') }
  'business-backend-python-010' = @{ matched = @('PostgreSQL|数据库', 'pytest|测试'); gap = @('FastAPI', '异步') }
  'business-mobile-android-011' = @{ matched = @('Kotlin', 'Compose'); gap = @('性能', '上架|发布') }
  'business-mobile-ios-012' = @{ matched = @('Swift', 'XCTest|测试'); gap = @('发布') }
  'business-test-auto-013' = @{ matched = @('pytest|接口', 'GitHub Actions|CI'); gap = @('性能|压测') }
  'business-test-manual-014' = @{ matched = @('功能测试', 'SQL'); gap = @('接口测试') }
  'business-data-engineer-015' = @{ matched = @('SQL', 'Pandas|清洗'); gap = @('Spark', '调度') }
  'business-data-analyst-016' = @{ matched = @('SQL', 'Tableau|可视化'); gap = @('A/B|实验') }
  'business-ml-engineer-017' = @{ matched = @('PyTorch'); gap = @('部署', '监控') }
  'business-llm-app-018' = @{ matched = @('Prompt|提示词', 'RAG|检索'); gap = @('工具调用', '成本') }
  'business-devops-019' = @{ matched = @('Linux', 'Docker'); gap = @('Kubernetes', 'CI/CD') }
  'business-sre-020' = @{ matched = @('Prometheus|指标'); gap = @('故障|值班', '容量') }
  'business-cloud-021' = @{ matched = @('Kubernetes', 'Helm'); gap = @('服务网格', '多租户') }
  'business-security-022' = @{ matched = @('SQL 注入|注入'); gap = @('渗透') }
  'business-network-023' = @{ matched = @('VLAN|路由', 'Wireshark|抓包'); gap = @('排障|生产', '自动化') }
  'business-dba-024' = @{ matched = @('MySQL|索引'); gap = @('备份', '复制|主从', '高可用') }
  'business-embedded-025' = @{ matched = @('STM32', 'UART'); gap = @('量产|硬件调试') }
  'business-game-026' = @{ matched = @('Unity', 'Profiler|性能'); gap = @('同步|联机|多人') }
  'business-algorithm-027' = @{ matched = @('C\+\+', '算法'); gap = @('落地|生产|工程') }
  'business-product-tech-028' = @{ matched = @('需求', 'API|接口'); gap = @('指标|用户数据', '跨团队|上线') }
}

$cases = @(Get-Content (Join-Path $PSScriptRoot 'minimal-eval-set.jsonl') -Encoding UTF8 |
  Where-Object { $_.Trim() } | ForEach-Object { $_ | ConvertFrom-Json } |
  Where-Object { $expectations.ContainsKey($_.id) })

foreach ($case in $cases) {
  $body = @{ jobDescription = $case.job; profile = $case.profile } | ConvertTo-Json
  $watch = [System.Diagnostics.Stopwatch]::StartNew()
  $status = 'none'
  $matched = 'n/a'
  $gap = 'n/a'
  try {
    $report = Invoke-RestMethod -Uri $Url -Method Post -ContentType 'application/json; charset=utf-8' -Body $body -TimeoutSec 40
    $status = 200
    $matchedText = @($report.evidence | Where-Object { $_.matched -eq $true } |
      ForEach-Object { "$($_.requirement) $($_.evidence)" }) -join ' '
    $gapText = @($report.skillGaps | ForEach-Object { "$($_.skill) $($_.reason)" }) +
      @($report.evidence | Where-Object { $_.matched -eq $false } |
        ForEach-Object { "$($_.requirement) $($_.evidence)" }) -join ' '
    $matched = @($expectations[$case.id].matched | Where-Object { $matchedText -match $_ }).Count
    $gap = @($expectations[$case.id].gap | Where-Object { $gapText -match $_ }).Count
  } catch {
    if ($_.Exception.Response) {
      $status = [int] $_.Exception.Response.StatusCode
    }
  } finally {
    $watch.Stop()
  }
  'case={0} status={1} elapsedMs={2} matched={3}/{4} gap={5}/{6}' -f $case.id, $status,
    $watch.ElapsedMilliseconds, $matched, $expectations[$case.id].matched.Count, $gap, $expectations[$case.id].gap.Count
}
