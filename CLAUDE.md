# 야미로그 (YamiLog) — CLAUDE.md

> 이 파일은 Claude Code가 프로젝트를 이해하고 일관된 코드를 생성하기 위한
> **단일 진실 공급원(Single Source of Truth)**이다.
> 코드 생성 전 반드시 이 파일을 참조할 것.

---

## 1. 프로젝트 개요

**야미로그**는 매니아들의 전문 리뷰 플랫폼이다.
일반인의 별점이 아닌, 검증된 매니아(레벨 시스템 자동 산정)의 평가만 노출하는 큐레이션 구조가 핵심이다.

- PRD 전문: `docs/specs/PRD.md`
- 아키텍처 결정 기록(ADR): `docs/adr/`
- API 스펙: `docs/specs/api/`

---

## 2. 기술 스택 (변경 금지)

| 영역 | 스택 |
|------|------|
| 언어 | Java 21 (Virtual Threads 적극 활용) |
| 프레임워크 | Spring Boot 3.3.5 LTS |
| 빌드 | Multi-Module Gradle 8.x (Kotlin DSL) |
| 아키텍처 | Hexagonal Architecture + CQRS |
| 서비스 간 통신 | REST (동기) + Apache Kafka (비동기 이벤트) |
| 공통 DB | PostgreSQL 16 (user/place/category/ad) |
| 문서형 DB | MongoDB 7 (review/feed) |
| 캐시 | Redis 7 |
| 검색 | Elasticsearch 8 |
| 프론트엔드 | Next.js 15 (App Router) + TypeScript 5 + Tailwind CSS v4 |

---

## 3. 모듈 구조

```
yamilog/
├── CLAUDE.md                        ← 지금 이 파일
├── settings.gradle.kts
├── build.gradle.kts                 ← 루트: 버전 BOM 중앙 관리
├── gradle.properties
│
├── common/
│   ├── common-domain/               ← 공유 VO, 이벤트 인터페이스, 예외 계층
│   └── common-infra/                ← Kafka 설정, JWT, SecurityFilter, ApiResponse
│
├── gateway/                         ← Spring Cloud Gateway (WebFlux)
│
├── services/
│   ├── user-service/                ← PostgreSQL, Redis, OAuth2
│   ├── category-service/            ← PostgreSQL, Redis (스키마 캐시)
│   ├── place-service/               ← PostgreSQL+PostGIS, Kakao/Naver API
│   ├── review-service/              ← MongoDB, Redis, S3
│   ├── feed-service/                ← MongoDB, Redis
│   ├── level-engine/                ← PostgreSQL, Redis, Spring Batch
│   ├── search-service/              ← Elasticsearch
│   └── ad-service/                  ← PostgreSQL, Redis
│
├── frontend/                        ← Next.js 15
│
└── docs/
    ├── specs/                       ← PRD, API 스펙
    └── adr/                         ← Architecture Decision Records
```

---

## 4. 헥사고날 아키텍처 패키지 구조 (모든 서비스 동일)

> **이 구조를 절대 변경하지 말 것.** 새 서비스 추가 시 동일 구조를 따른다.

> **패키지 루트 규칙**: `com.yamilog.{servicename}` — 서비스 디렉터리명에서 하이픈 제거
> 예: `user-service` → `com.yamilog.userservice`, `level-engine` → `com.yamilog.levelengine`

```
com.yamilog.{servicename}/
├── domain/
│   ├── model/          # 엔티티, 값 객체 (외부 의존성 0)
│   └── event/          # 도메인 이벤트 정의
├── application/
│   ├── port/
│   │   ├── in/         # UseCase 인터페이스 (Command/Query)
│   │   └── out/        # Repository/외부서비스 인터페이스
│   └── service/        # UseCase 구현체 (비즈니스 로직)
└── adapter/
    ├── in/
    │   ├── web/        # @RestController (요청/응답 DTO 여기서만)
    │   └── messaging/  # @KafkaListener
    └── out/
        ├── persistence/ # JPA / MongoRepository 구현
        ├── messaging/   # KafkaTemplate 발행
        └── external/    # 외부 API 클라이언트 (WebClient)
```

### 의존성 방향 규칙

```
adapter/in  →  application/port/in  →  application/service
                                              ↓
adapter/out  ←  application/port/out  ←  (구현)
```

- `domain` 은 어떤 레이어도 의존하지 않는다.
- `application` 은 `domain` 만 의존한다. Spring 어노테이션 최소화.
- `adapter` 는 `application/port` 인터페이스만 호출한다. 구현체 직접 참조 금지.
- `common-infra` 빈(`JwtProvider`, `GlobalExceptionHandler` 등)은 **Spring Boot AutoConfiguration**으로 자동 등록된다. 각 서비스에서 `@ComponentScan` 확장 불필요.

---

## 5. 코딩 컨벤션

### 5.1 공통

- **Lombok 적극 사용**: `@RequiredArgsConstructor`, `@Value`, `@Builder`, `@Getter`
- **불변 객체 우선**: 도메인 모델은 가능한 한 불변으로 설계
- **생성자 주입만 허용**: `@Autowired` 필드 주입 금지
- **`@Transactional` 위치**: Application Service 메서드에만 선언
- **`Optional` 반환**: Repository에서 단건 조회 시 `Optional<T>` 반환
- **레코드 타입**: Command/Query DTO는 Java Record 사용 권장

### 5.2 네이밍

| 대상 | 컨벤션 | 예시 |
|------|--------|------|
| UseCase 인터페이스 | `{동사}{명사}UseCase` | `CreateReviewUseCase` |
| Command | `{동사}{명사}Command` | `CreateReviewCommand` |
| Query | `{동사}{명사}Query` | `FindPlacesByLocationQuery` |
| 도메인 이벤트 | `{명사}{과거동사}Event` | `ReviewCreatedEvent` |
| Repository Port | `{명사}Repository` | `ReviewRepository` |
| Controller | `{명사}Controller` | `ReviewController` |
| Request DTO | `{동사}{명사}Request` | `CreateReviewRequest` |
| Response DTO | `{명사}Response` | `ReviewResponse` |
| JPA Entity | `{명사}Entity` (suffix) | `ReviewEntity` |
| Mongo Document | `{명사}Document` (suffix) | `ReviewDocument` |

### 5.3 API 설계

- 버전: `/api/v1/` 접두사 필수
- 복수 명사: `/api/v1/reviews`, `/api/v1/places`
- HTTP 메서드: GET(조회), POST(생성), PUT(전체수정), PATCH(부분수정), DELETE(삭제)
- 응답 포맷: 항상 `ApiResponse<T>` 래퍼 사용 (common-infra 제공)

```java
// 응답 예시
ApiResponse.success(reviewResponse)           // 200
ApiResponse.created(reviewResponse)           // 201
ApiResponse.error("REVIEW_NOT_FOUND", msg)    // 4xx
```

### 5.4 예외 처리

- 도메인 예외: `DomainException` 상속 (common-domain)
- 비즈니스 예외: `BusinessException` + `ErrorCode` enum
- 글로벌 핸들러: `GlobalExceptionHandler` (common-infra) 가 자동 처리
- 예외 메시지는 한국어로 작성

---

## 6. Kafka 이벤트 컨벤션

### 토픽 네이밍
```
yamilog.{도메인}.{이벤트}
예: yamilog.review.created
    yamilog.level.changed
    yamilog.place.created
```

### 이벤트 구조 (모든 이벤트 공통)
```java
public record ReviewCreatedEvent(
    String eventId,          // UUID
    String eventType,        // "ReviewCreated"
    Instant occurredAt,      // 발생 시각
    String aggregateId,      // 리뷰 ID
    // ... 페이로드
) implements DomainEvent {}
```

### 발행/구독 서비스 매핑
| 이벤트 | 발행 | 구독 |
|--------|------|------|
| `yamilog.review.created` | review-service | level-engine, feed-service, search-service |
| `yamilog.review.updated` | review-service | level-engine, search-service |
| `yamilog.level.changed` | level-engine | user-service, feed-service |
| `yamilog.place.created` | place-service | search-service, feed-service |
| `yamilog.user.followed` | user-service | feed-service |

---

## 7. 테스트 전략

### 계층별 테스트

| 계층 | 종류 | 도구 |
|------|------|------|
| 아키텍처 규칙 | 아키텍처 테스트 | ArchUnit (`HexagonalArchitectureTest`) |
| Domain | 단위 테스트 | JUnit 5, 외부 의존성 0 |
| Application Service | 단위 테스트 | JUnit 5 + Mockito |
| Adapter/in/web | 슬라이스 테스트 | `@WebMvcTest` |
| Adapter/out/persistence | 슬라이스 테스트 | `@DataJpaTest`, `@DataMongoTest` |
| 통합 테스트 | Testcontainers | PostgreSQL/MongoDB/Redis/Kafka 실 컨테이너 |

### ArchUnit 아키텍처 테스트

각 서비스는 `arch/HexagonalArchitectureTest.java` 를 반드시 포함해야 한다.
규칙 정의는 `common-domain` testFixtures 의 `HexagonalArchRules` 에서 중앙 관리한다.

```java
// 새 서비스 추가 시 ROOT 패키지만 교체
@AnalyzeClasses(packages = HexagonalArchitectureTest.ROOT)
class HexagonalArchitectureTest {
    static final String ROOT = "com.yamilog.{servicename}";

    @ArchTest static final ArchRule domainNotDependOnAdapter =
        HexagonalArchRules.domainShouldNotDependOnAdapter(ROOT);
    // ... (나머지 규칙 동일)
}
```

적용 규칙:
- `domain` → `adapter` 의존 금지
- `domain` → `application` 의존 금지
- `application` → `adapter` 의존 금지
- `adapter.in` → `adapter.out` 직접 의존 금지
- `domain.model` → Spring 어노테이션 금지
- `domain.model` → JPA 어노테이션 금지 (`@Entity` 는 `*Entity` 클래스에만)
- `application` → Servlet API 금지

### 테스트 명명 규칙
```java
@Test
@DisplayName("리뷰 생성 시 ReviewCreatedEvent가 발행된다")
void createReview_publishesReviewCreatedEvent() { ... }
```

### 커버리지 목표
- Domain + Application: **80% 이상**
- Adapter: **60% 이상**
- 전체: **70% 이상**

---

## 8. Git 컨벤션

### 브랜치 전략 (GitHub Flow 기반)
```
main                    ← 항상 배포 가능 상태
  └── feat/review-crud  ← 기능 개발
  └── fix/level-calc    ← 버그 수정
  └── refactor/feed-q   ← 리팩터링
  └── chore/deps-bump   ← 의존성/설정
```

### 커밋 메시지 (Conventional Commits)
```
feat(review): 리뷰 작성 API 구현
fix(level): 레벨 다운 계산 오류 수정
refactor(feed): 피드 조회 쿼리 N+1 해결
test(user): 팔로우 통합 테스트 추가
chore(deps): Spring Boot 3.3.5 업그레이드
docs(adr): MongoDB 선택 ADR 추가
```

---

## 9. 로컬 개발 환경

### 인프라 기동 (Docker Compose)
```powershell
# 전체 인프라 기동
docker compose -f docker/docker-compose.local.yml up -d

# 개별 서비스 기동
docker compose -f docker/docker-compose.local.yml up -d postgres mongodb redis kafka elasticsearch
```

### 서비스 포트 맵
| 서비스 | 포트 |
|--------|------|
| Gateway | 8080 |
| user-service | 8081 |
| category-service | 8082 |
| place-service | 8083 |
| review-service | 8084 |
| feed-service | 8085 |
| level-engine | 8086 |
| search-service | 8087 |
| ad-service | 8088 |
| PostgreSQL | 5432 |
| MongoDB | 27017 |
| Redis | 6379 |
| Kafka | 9092 |
| Elasticsearch | 9200 |
| Kafka UI | 8989 |

### Gradle 빌드
```powershell
# 전체 빌드
.\gradlew.bat build

# 특정 서비스만 빌드
.\gradlew.bat :services:user-service:build

# 테스트 제외 빠른 빌드
.\gradlew.bat :services:user-service:build -x test

# 전체 테스트
.\gradlew.bat test

# 특정 서비스 테스트
.\gradlew.bat :services:review-service:test
```

---

## 10. Claude Code 작업 가이드

### 새 기능 구현 순서 (반드시 이 순서를 따를 것)

```
1. Domain Model 작성        (domain/model/)
2. Domain Event 정의        (domain/event/)
3. Inbound Port 정의        (application/port/in/)
4. Outbound Port 정의       (application/port/out/)
5. Application Service 구현 (application/service/)
6. Persistence Adapter 구현 (adapter/out/persistence/)
7. Messaging Adapter 구현   (adapter/out/messaging/)
8. Web Adapter 구현         (adapter/in/web/)
9. 단위 테스트 작성
10. 통합 테스트 작성

신규 서비스 추가 시 추가 필수 작업:
- build.gradle.kts 에 testImplementation(testFixtures(project(":common:common-domain"))) 추가
- src/test/.../arch/HexagonalArchitectureTest.java 작성 (ROOT 패키지만 교체)
```

### Claude Code 슬래시 커맨드

| 커맨드 | 역할 |
|--------|------|
| `/new-service` | 새 마이크로서비스 스캐폴딩 |
| `/new-feature` | 특정 서비스에 신규 기능 추가 |
| `/new-event` | Kafka 이벤트 + 발행/구독 코드 생성 |
| `/review-arch` | 코드의 헥사고날 원칙 준수 여부 검토 |
| `/gen-test` | 현재 파일의 테스트 자동 생성 |
| `/gen-api-spec` | 컨트롤러 기반 OpenAPI 스펙 생성 |

커맨드 정의: `.claude/commands/`

### Claude Code 작업 시 주의사항

1. **도메인 모델에 Spring 어노테이션 추가 금지** (`@Entity`는 persistence adapter 계층의 `*Entity`에만)
2. **Application Service에서 HTTP 관련 객체 사용 금지** (`HttpServletRequest` 등)
3. **Adapter에서 다른 Adapter 직접 호출 금지** (반드시 Port 인터페이스 통해서)
4. **`@Transactional`을 도메인 모델에 선언 금지**
5. **공통 모듈 변경 시 반드시 영향 받는 서비스 전체 테스트 실행**

---

## 11. IntelliJ IDEA 설정

`.idea/` 설정 파일은 팀 공유 대상:
- `codeStyleSettings.xml` — Google Java Style 기반
- `inspectionProfiles/` — 헥사고날 의존성 위반 경고
- `runConfigurations/` — 각 서비스 Run/Debug 설정

코드 스타일 Import: `Settings > Editor > Code Style > Java > Import Scheme`
대상 파일: `docs/dev/intellij-code-style.xml`

---

## 12. 도메인 핵심 규칙 (코드 생성 시 반드시 반영)

### 12.1 레벨 시스템 상수

레벨 값은 코드 전체에서 아래 enum을 사용한다. 숫자 하드코딩 금지.

```java
public enum ManiaLevel {
    NEWBIE(0),
    ENTHUSIAST(1),
    MANIA(2),
    EXPERT(3),
    MASTER(4);

    private final int value;

    // 퀄리티 점수 임계값
    public static final Map<ManiaLevel, Integer> QUALITY_THRESHOLD = Map.of(
        ENTHUSIAST, 60,
        MANIA,      75,
        EXPERT,     85,
        MASTER,     90
    );

    // 리뷰 수 임계값
    public static final Map<ManiaLevel, Integer> REVIEW_THRESHOLD = Map.of(
        ENTHUSIAST,  5,
        MANIA,      20,
        EXPERT,     50,
        MASTER,    100
    );
}
```

퀄리티 점수 가중치 (변경 시 PRD 동시 수정):
- 리뷰 길이: **30%**
- 항목 작성 완성도: **30%**
- 유용성 투표(helpful): **40%**

레벨 강등 규칙:
- 90일 미활동 → 경고 알림
- 180일 미활동 → 한 단계 강등

### 12.2 리뷰 가시성 (VisibilityLevel)

```java
public enum VisibilityLevel {
    PUBLIC(0),       // 전체 공개
    MANIA(2),        // Mania 이상만
    EXPERT(3);       // Expert 이상만

    private final int requiredLevel;

    public boolean isAccessibleBy(ManiaLevel userLevel) {
        return userLevel.getValue() >= this.requiredLevel;
    }
}
```

API 응답 시 접근자 레벨 기준으로 `VisibilityLevel` 필터링 필수.
필터링은 Application Service 계층에서 처리한다 (Controller/Adapter 아님).

### 12.3 카테고리 스키마 구조

카테고리별 평가 항목은 DB(category-service)에서 관리한다.
코드에 하드코딩 금지. 항상 category-service에서 조회 후 사용.

```java
// 카테고리 스키마 예시 (DB에 저장되는 구조)
record CategorySchema(
    String categoryId,
    String name,                          // "필터 커피"
    List<EvaluationField> fields          // 평가 항목 목록
) {}

record EvaluationField(
    String fieldKey,                      // "acidity"
    String displayName,                   // "산미"
    FieldType type,                       // SCORE_1_10 / TEXT / SELECT
    List<String> options,                 // SELECT 타입일 때 선택지
    boolean required
) {}
```

리뷰 저장 시 카테고리 평가 항목은 `Map<String, Object> evaluationData`로 MongoDB에 저장.

### 12.4 서비스 간 동기 호출 정책

동기 호출이 허용되는 경우만 명시. 나머지는 **Kafka 이벤트**로만 통신.

| 호출 방향 | 방식 | 비고 |
|-----------|------|------|
| place-service → ad-service | REST (WebClient) | 검색 결과 상단 슬롯 조회 |
| review-service → category-service | REST (WebClient) | 리뷰 작성 시 스키마 유효성 검증 |
| gateway → 각 서비스 | REST | 게이트웨이 라우팅 |

WebClient 호출 시 Resilience4j CircuitBreaker 필수 적용.
타임아웃: Connection 3s, Read 5s.

### 12.5 데이터베이스 네이밍 컨벤션

**PostgreSQL (JPA)**
- 테이블명: `snake_case` 복수형 (예: `users`, `review_helpful_votes`)
- 컬럼명: `snake_case` (예: `created_at`, `mania_level`)
- PK: `id` (UUID, `uuid-ossp` 생성)
- 생성/수정 시각: `created_at`, `updated_at` 모든 테이블 필수
- JPA Entity에 `@Table(name = "...")` 명시 필수

**MongoDB (Spring Data)**
- 컬렉션명: `snake_case` 복수형 (예: `reviews`, `feed_items`)
- Document에 `@Document(collection = "...")` 명시 필수
- `_id`: String (UUID)
- 생성 시각: `createdAt` (camelCase, MongoDB 관례)

**Redis Key 네이밍**
```
yamilog:{서비스}:{대상}:{id}
예: yamilog:user:level:usr_abc123
    yamilog:review:count:usr_abc123:coffee
    yamilog:place:slot:seoul:coffee
```
TTL은 application.yml에서 환경변수로 관리, 코드 하드코딩 금지.

### 12.6 보안 정책 — API 레벨별 접근 제한

Spring Security + JWT 기반. Gateway에서 1차 인증, 각 서비스에서 2차 인가.

```java
// 커스텀 어노테이션 사용 (common-infra 제공)
@RequireLevel(ManiaLevel.ENTHUSIAST)  // 장소 등록: Enthusiast 이상
@RequireLevel(ManiaLevel.MANIA)       // 매니아 공개 리뷰 작성
@RequireLevel(ManiaLevel.NEWBIE)      // 일반 리뷰 작성: 누구나

// 공개 API (인증 불필요)
@Public  // 장소 조회, 공개 리뷰 조회, 카테고리 조회
```

JWT Payload에 포함되는 클레임:
```json
{
  "sub": "usr_abc123",
  "nickname": "밀턴",
  "levels": { "coffee": 2, "whiskey": 1 },
  "exp": 1234567890
}
```
레벨은 카테고리별로 독립적으로 관리된다.

### 12.7 페이지네이션 전략

**목록 조회 API는 무조건 커서 기반 페이지네이션 사용.** `Pageable` 오프셋 방식 금지 (대용량 피드에서 성능 문제).

```java
// 커서 기반 요청
record CursorPageRequest(
    String cursor,      // null이면 첫 페이지
    int size            // 기본 20, 최대 50
) {}

// 커서 기반 응답
record CursorPageResponse<T>(
    List<T> items,
    String nextCursor,  // null이면 마지막 페이지
    boolean hasNext
) {}
```

커서 값: `Base64(id + ":" + createdAt)` 형태로 인코딩.
예외: 관리자 페이지, 통계 조회는 `Pageable` 허용.

---

## 13. 현재 구현 상태 (진행 추적)

> Claude Code가 작업 시작 전 반드시 확인하는 섹션.
> 구현 완료 시 상태를 업데이트할 것.

| 서비스 | Domain | Application | Adapter | 테스트 | 상태 |
|--------|--------|-------------|---------|--------|------|
| common-domain | ✅ | - | - | ✅ | 완료 |
| common-infra | ✅ | - | - | ✅ | 완료 |
| gateway | - | ⬜ | ⬜ | ⬜ | 미시작 |
| user-service | ✅ | ✅ | ✅ | ✅ | 완료 |
| category-service | ⬜ | ⬜ | ⬜ | ⬜ | 미시작 |
| place-service | ⬜ | ⬜ | ⬜ | ⬜ | 미시작 |
| review-service | ⬜ | ⬜ | ⬜ | ⬜ | 미시작 |
| feed-service | ⬜ | ⬜ | ⬜ | ⬜ | 미시작 |
| level-engine | ⬜ | ⬜ | ⬜ | ⬜ | 미시작 |
| search-service | ⬜ | ⬜ | ⬜ | ⬜ | 미시작 |
| ad-service | ⬜ | ⬜ | ⬜ | ⬜ | 미시작 |

범례: ⬜ 미시작 / 🔄 진행중 / ✅ 완료
