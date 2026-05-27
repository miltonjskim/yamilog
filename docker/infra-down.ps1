# 야미로그 로컬 인프라 종료 스크립트

$ProjectRoot = Split-Path -Parent $PSScriptRoot

Write-Host "[야미로그] 로컬 인프라 종료 중..." -ForegroundColor Yellow

docker compose -f "$ProjectRoot\docker\docker-compose.local.yml" down

if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ 인프라 종료 완료" -ForegroundColor Green
}
