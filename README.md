# 야미로그 개발 시작 가이드

## 빠른 시작 (5분)

### 1. 사전 요구사항

| 도구 | 버전 | 설치 링크 |
|------|------|-----------|
| Java (Temurin) | 21 LTS | https://adoptium.net/temurin/releases/?version=21 |
| Docker Desktop | 최신 | https://www.docker.com/products/docker-desktop |
| IntelliJ IDEA | Ultimate | https://www.jetbrains.com/idea/download |
| Claude Code | 최신 | `npm install -g @anthropic-ai/claude-code` |
| Git | 최신 | https://git-scm.com/download/win |
| Node.js | 20 LTS | https://nodejs.org/en/download |

### 2. 환경 셋업

```powershell
# 저장소 클론
git clone https://github.com/your-org/yamilog.git
cd yamilog

# 개발 환경 자동 셋업 (PowerShell 관리자 권한)
.\setup-dev.ps1
```

### 3. IntelliJ에서 열기

1. `File > Open` → `yamilog` 폴더 선택
2. Gradle import 완료까지 대기
3. `File > Project Structure > SDKs` → Java 21 확인
4. 우상단 Run Configuration 목록에서 `🐳 Infra Up` 실행

### 4. Claude Code 시작

```powershell
# 프로젝트 루트에서
claude

# 첫 대화 예시
> CLAUDE.md를 읽고 프로젝트 구조를 파악해줘
> /new-feature user-service 팔로우-팔로워
```

---

## 디렉토리 구조

```
yamilog/
├── CLAUDE.md              ← Claude Code 작업 가이드 (반드시 읽을 것)
├── .claude/commands/      ← 슬래시 커맨드 정의
├── docs/specs/            ← PRD, API 스펙
├── docker/                ← 로컬 개발 인프라
├── common/                ← 공유 모듈
├── gateway/               ← API 게이트웨이
├── services/              ← 마이크로서비스 8개
└── frontend/              ← Next.js 15
```

---

## 자주 쓰는 명령어

### Gradle

```powershell
# 특정 서비스 빌드 (테스트 제외)
.\gradlew.bat :services:user-service:build -x test

# 전체 테스트
.\gradlew.bat test

# 특정 서비스 테스트만
.\gradlew.bat :services:review-service:test

# 의존성 트리 확인
.\gradlew.bat :services:user-service:dependencies
```

### Docker

```powershell
# 인프라 전체 기동
docker compose -f docker\docker-compose.local.yml up -d

# 인프라 종료
docker compose -f docker\docker-compose.local.yml down

# 모니터링 도구 포함 기동 (Kibana, Redis Commander)
docker compose -f docker\docker-compose.local.yml --profile monitoring up -d

# 로그 확인
docker compose -f docker\docker-compose.local.yml logs -f kafka
```

### Claude Code 슬래시 커맨드

| 커맨드 | 설명 |
|--------|------|
| `/new-feature [서비스] [기능]` | 헥사고날 순서로 신규 기능 구현 |
| `/new-event [이벤트] [발행] [구독]` | Kafka 이벤트 발행/구독 코드 생성 |
| `/review-arch [경로]` | 아키텍처 원칙 준수 여부 검토 |
| `/gen-test [파일]` | 테스트 코드 자동 생성 |

---

## 서비스 포트

| 서비스 | 포트 | URL |
|--------|------|-----|
| Gateway | 8080 | http://localhost:8080 |
| user-service | 8081 | http://localhost:8081 |
| category-service | 8082 | http://localhost:8082 |
| place-service | 8083 | http://localhost:8083 |
| review-service | 8084 | http://localhost:8084 |
| feed-service | 8085 | http://localhost:8085 |
| level-engine | 8086 | http://localhost:8086 |
| search-service | 8087 | http://localhost:8087 |
| ad-service | 8088 | http://localhost:8088 |
| Kafka UI | 8989 | http://localhost:8989 |
| Elasticsearch | 9200 | http://localhost:9200 |

---

## 문서

- [PRD (제품 요구사항)](docs/specs/PRD.md)
- [아키텍처 결정 기록](docs/adr/)
- [API 스펙](docs/specs/api/)
- [Claude Code 가이드](CLAUDE.md)

---

## ⚠️ 최초 설정 필수: gradle-wrapper.jar 생성

`gradle/wrapper/gradle-wrapper.jar`는 바이너리 파일로 Git에 포함되지 않습니다.
프로젝트 클론 후 **한 번만** 실행하세요.

```powershell
# Java 21이 설치된 상태에서
# 방법 1: Gradle이 로컬에 설치된 경우
gradle wrapper --gradle-version 8.10.2

# 방법 2: IntelliJ에서 자동 생성
# File > Settings > Build > Gradle > Gradle JVM: Java 21
# Gradle 탭에서 "Reload All Gradle Projects" 클릭 시 자동 생성됨

# 방법 3: SDKMAN (WSL/Git Bash)
sdk install gradle 8.10.2
gradle wrapper --gradle-version 8.10.2
```

생성 확인:
```powershell
.\gradlew.bat --version
# 출력: Gradle 8.10.2
```

---

## IntelliJ Run Configuration 수동 설정

`.idea/runConfigurations/` XML이 로드되지 않을 경우 직접 설정합니다.

### 🐳 Infra Up (Docker Compose 기동)

```
Run > Edit Configurations > + > Shell Script
  Name        : 🐳 Infra Up
  Script path : (비워두기)
  Interpreter : cmd.exe
  Interpreter options: /c
  Script text : docker compose -f docker/docker-compose.local.yml up -d postgres mongodb redis zookeeper kafka kafka-ui elasticsearch
  Working dir : $PROJECT_DIR$
  ✅ Execute in terminal 체크
```

### 🐳 Infra Down

```
Run > Edit Configurations > + > Shell Script
  Name        : 🐳 Infra Down
  Script text : docker compose -f docker/docker-compose.local.yml down
  (나머지 동일)
```

### Spring Boot 서비스 실행 (예: user-service)

```
Run > Edit Configurations > + > Spring Boot
  Name            : user-service [local]
  Main class      : com.yamilog.userservice.UserServiceApplication
  Module          : yamilog.services.user-service.main
  VM options      : -Xmx512m -Dspring.profiles.active=local
  Environment vars:
    SERVER_PORT=8081
    DB_HOST=localhost
    DB_NAME=yamilog_user
    REDIS_HOST=localhost
    KAFKA_SERVERS=localhost:9092
```
