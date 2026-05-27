# ============================================================
# 야미로그 개발 환경 셋업 스크립트 (Windows PowerShell)
# 사용: PowerShell을 관리자 권한으로 실행 후 .\setup-dev.ps1
# ============================================================

$ErrorActionPreference = "Stop"
$ProjectRoot = $PSScriptRoot

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  야미로그 개발 환경 셋업" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# ── 1. 필수 도구 확인 ──────────────────────────────────────────────────────────
Write-Host "[1/6] 필수 도구 확인 중..." -ForegroundColor Yellow

$tools = @(
    @{ Name = "Java 21"; Command = "java"; Args = "-version"; MinVersion = "21" },
    @{ Name = "Docker"; Command = "docker"; Args = "--version"; MinVersion = "" },
    @{ Name = "Git"; Command = "git"; Args = "--version"; MinVersion = "" },
    @{ Name = "Node.js"; Command = "node"; Args = "--version"; MinVersion = "20" }
)

foreach ($tool in $tools) {
    try {
        $output = & $tool.Command $tool.Args 2>&1
        Write-Host "  ✅ $($tool.Name) 확인됨" -ForegroundColor Green
    } catch {
        Write-Host "  ❌ $($tool.Name) 미설치 — 설치 후 재실행하세요" -ForegroundColor Red
        if ($tool.Name -eq "Java 21") {
            Write-Host "     → https://adoptium.net/temurin/releases/?version=21" -ForegroundColor Gray
        }
        exit 1
    }
}

# Claude Code 확인
try {
    $claudeVersion = & claude --version 2>&1
    Write-Host "  ✅ Claude Code 확인됨: $claudeVersion" -ForegroundColor Green
} catch {
    Write-Host "  ⚠️  Claude Code 미설치 — npm install -g @anthropic-ai/claude-code" -ForegroundColor Yellow
}

# ── 2. Docker 인프라 기동 ────────────────────────────────────────────────────
Write-Host ""
Write-Host "[2/6] 로컬 인프라 기동 중 (Docker Compose)..." -ForegroundColor Yellow

Set-Location "$ProjectRoot\docker"
docker compose -f docker-compose.local.yml up -d

if ($LASTEXITCODE -ne 0) {
    Write-Host "  ❌ Docker Compose 기동 실패" -ForegroundColor Red
    exit 1
}
Write-Host "  ✅ 인프라 기동 완료" -ForegroundColor Green

# ── 3. 인프라 헬스체크 ──────────────────────────────────────────────────────
Write-Host ""
Write-Host "[3/6] 인프라 헬스체크 (최대 60초)..." -ForegroundColor Yellow

Set-Location $ProjectRoot

$services = @(
    @{ Name = "PostgreSQL"; Port = 5432 },
    @{ Name = "MongoDB"; Port = 27017 },
    @{ Name = "Redis"; Port = 6379 },
    @{ Name = "Kafka"; Port = 9092 },
    @{ Name = "Elasticsearch"; Port = 9200 }
)

foreach ($svc in $services) {
    $maxRetry = 12
    $retry = 0
    while ($retry -lt $maxRetry) {
        try {
            $tcp = New-Object System.Net.Sockets.TcpClient
            $tcp.Connect("localhost", $svc.Port)
            $tcp.Close()
            Write-Host "  ✅ $($svc.Name) (:$($svc.Port)) 준비됨" -ForegroundColor Green
            break
        } catch {
            $retry++
            if ($retry -eq $maxRetry) {
                Write-Host "  ⚠️  $($svc.Name) 응답 없음 — 수동 확인 필요" -ForegroundColor Yellow
            } else {
                Start-Sleep -Seconds 5
            }
        }
    }
}

# ── 4. Gradle Wrapper 확인 및 빌드 ──────────────────────────────────────────
Write-Host ""
Write-Host "[4/6] Gradle 의존성 다운로드 중..." -ForegroundColor Yellow

Set-Location $ProjectRoot
& .\gradlew.bat dependencies --quiet 2>&1 | Out-Null

if ($LASTEXITCODE -ne 0) {
    Write-Host "  ❌ Gradle 의존성 해결 실패" -ForegroundColor Red
} else {
    Write-Host "  ✅ Gradle 의존성 준비 완료" -ForegroundColor Green
}

# ── 5. Frontend 의존성 설치 ───────────────────────────────────────────────────
Write-Host ""
Write-Host "[5/6] Frontend 의존성 설치 중 (npm install)..." -ForegroundColor Yellow

if (Test-Path "$ProjectRoot\frontend\package.json") {
    Set-Location "$ProjectRoot\frontend"
    npm install --silent
    if ($LASTEXITCODE -eq 0) {
        Write-Host "  ✅ Frontend 의존성 설치 완료" -ForegroundColor Green
    } else {
        Write-Host "  ⚠️  npm install 실패 — frontend 디렉토리 확인 필요" -ForegroundColor Yellow
    }
} else {
    Write-Host "  ⏭️  frontend/package.json 없음 — 스킵" -ForegroundColor Gray
}

# ── 6. 완료 안내 ─────────────────────────────────────────────────────────────
Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "  ✅ 개발 환경 셋업 완료!" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "🚀 다음 단계:" -ForegroundColor White
Write-Host ""
Write-Host "  [IntelliJ]" -ForegroundColor Yellow
Write-Host "  1. File > Open > yamilog 폴더 선택" -ForegroundColor Gray
Write-Host "  2. Gradle 자동 import 확인" -ForegroundColor Gray
Write-Host "  3. File > Project Structure > SDK: Java 21 설정" -ForegroundColor Gray
Write-Host ""
Write-Host "  [Claude Code]" -ForegroundColor Yellow
Write-Host "  1. 프로젝트 루트에서: claude" -ForegroundColor Gray
Write-Host "  2. CLAUDE.md 자동 로드됨" -ForegroundColor Gray
Write-Host "  3. 사용 가능한 커맨드:" -ForegroundColor Gray
Write-Host "     /new-feature, /new-event, /review-arch, /gen-test" -ForegroundColor Cyan
Write-Host ""
Write-Host "  [로컬 서비스 URL]" -ForegroundColor Yellow
Write-Host "  Kafka UI      : http://localhost:8989" -ForegroundColor Gray
Write-Host "  Elasticsearch : http://localhost:9200" -ForegroundColor Gray
Write-Host "  Redis (CLI)   : redis-cli -p 6379" -ForegroundColor Gray
Write-Host ""
Write-Host "  [인프라 종료]" -ForegroundColor Yellow
Write-Host "  docker compose -f docker\docker-compose.local.yml down" -ForegroundColor Gray
Write-Host ""

Set-Location $ProjectRoot
