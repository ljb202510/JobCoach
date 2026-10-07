param(
  [ValidateRange(1, 20)]
  [int] $Count = 1,
  [string] $Url = 'http://localhost:8080/api/matches',
  [string[]] $CaseId = @('backend-evidence-005', 'career-switch-006', 'frontend-gap-007'),
  [switch] $AllBusinessCases
)

$cases = @(Get-Content (Join-Path $PSScriptRoot 'minimal-eval-set.jsonl') -Encoding UTF8 |
  Where-Object { $_.Trim() } | ForEach-Object { $_ | ConvertFrom-Json })
if ($AllBusinessCases) {
  $CaseId = @($cases | Where-Object { $_.id -match '^(backend-evidence|career-switch|frontend-gap|business-)' } |
    ForEach-Object { $_.id })
}
foreach ($id in $CaseId) {
  if (-not ($cases | Where-Object { $_.id -eq $id })) {
    throw "Unknown case id: $id"
  }
}

foreach ($case in ($cases | Where-Object { $CaseId -contains $_.id })) {
  $body = @{ jobDescription = $case.job; profile = $case.profile } | ConvertTo-Json
  for ($index = 1; $index -le $Count; $index++) {
    $watch = [System.Diagnostics.Stopwatch]::StartNew()
    $status = 'none'
    $result = 'network_error'

    try {
      $response = Invoke-WebRequest -Uri $Url -Method Post -ContentType 'application/json; charset=utf-8' -Body $body -UseBasicParsing
      $status = [int] $response.StatusCode
      $report = $response.Content | ConvertFrom-Json
      $valid = $null -ne $report.matchScore -and $report.matchScore -ge 0 -and $report.matchScore -le 100 -and
        $null -ne $report.requirements -and $null -ne $report.evidence -and $null -ne $report.skillGaps -and
        $null -ne $report.risks -and $null -ne $report.recommendations
      $result = if ($valid) { 'structure_ok' } else { 'invalid_response' }
      $narrative = @($report.evidence | ForEach-Object { $_.evidence }) +
        @($report.skillGaps | ForEach-Object { $_.reason }) + @($report.risks) + @($report.recommendations)
      $chinese = if ($narrative.Count -eq 0) { 'no_narrative' } elseif (@($narrative | Where-Object { $_ -notmatch '\p{IsCJKUnifiedIdeographs}' }).Count -eq 0) { 'yes' } else { 'no' }
      $unmatched = @($report.evidence | Where-Object { $_.matched -eq $false }).Count
    } catch {
      if ($_.Exception.Response) {
        $status = [int] $_.Exception.Response.StatusCode
        $result = if ($status -eq 504) { 'timeout' } else { 'http_error' }
      }
    } finally {
      $watch.Stop()
    }

    'case={0} attempt={1} status={2} elapsedMs={3} result={4} chinese={5} unmatched={6}' -f $case.id, $index, $status, $watch.ElapsedMilliseconds, $result, $(if ($status -eq 200) { $chinese } else { 'n/a' }), $(if ($status -eq 200) { $unmatched } else { 'n/a' })
  }
}
