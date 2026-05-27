# gateway — 서비스 컨텍스트

> 루트 CLAUDE.md와 함께 읽을 것.

## 역할
모든 클라이언트 요청의 단일 진입점. 인증, 라우팅, Rate Limiting, CORS.

## 기술 스택
- Spring Cloud Gateway (WebFlux 기반 — Servlet 아님)
- **`common-infra` 미사용** (`spring-boot-starter-web`과 WebFlux 충돌)
- `common-domain`만 직접 의존

## 패키지 루트
`com.yamilog.gateway`

## 주요 필터

### JwtAuthFilter (GlobalFilter)
1. `Authorization: Bearer {token}` 헤더 추출
2. JWT 서명 검증
3. 검증 성공 시 `X-User-Id`, `X-User-Level` 헤더를 downstream에 추가
4. 검증 실패 시 401 반환

### 공개 경로 (인증 불필요)
```
GET  /api/v1/places/**       (장소 조회)
GET  /api/v1/categories/**   (카테고리 조회)
POST /api/v1/auth/login      (로그인)
POST /api/v1/auth/register   (회원가입)
GET  /api/v1/search/**       (검색)
```

## 주의사항
- WebFlux 환경: 모든 코드 Reactive(`Mono`, `Flux`) 또는 Virtual Thread 기반.
- `HttpServletRequest` 사용 금지. `ServerWebExchange` 사용.
- 각 서비스의 비즈니스 로직을 Gateway에 추가 금지. 순수 인프라 역할만.
