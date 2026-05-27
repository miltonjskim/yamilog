# 야미로그 로컬 인프라 기동 스크립트
# IntelliJ Run Configuration "🐳 Infra Up" 에서 실행되는 스크립트

$ProjectRoot = Split-Path -Parent $PSScriptRoot

Write-Host "[야미로그] 로컬 인프라 기동 중..." -ForegroundColor Cyan

docker compose -f "$ProjectRoot\docker\docker-compose.local.yml" up -d `
    postgres mongodb redis zookeeper kafka kafka-ui elasticsearch

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "✅ 인프라 기동 완료" -ForegroundColor Green
    Write-Host ""
    Write-Host "  Kafka UI      : http://localhost:8989" -ForegroundColor Gray
    Write-Host "  Elasticsearch : http://localhost:9200" -ForegroundColor Gray
    Write-Host "  PostgreSQL    : localhost:5432" -ForegroundColor Gray
    Write-Host "  MongoDB       : localhost:27017" -ForegroundColor Gray
    Write-Host "  Redis         : localhost:6379" -ForegroundColor Gray
} else {
    Write-Host "❌ 기동 실패 — docker compose logs 로 확인하세요" -ForegroundColor Red
}
