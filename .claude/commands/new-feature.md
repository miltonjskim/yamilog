새 기능을 헥사고날 아키텍처 순서대로 구현한다.

## 사용법
/new-feature [서비스명] [기능명]
예: /new-feature review-service 리뷰-유용성-투표

## 구현 순서 (반드시 이 순서를 따를 것)

다음 순서로 파일을 생성하라. 각 단계마다 코드를 작성하고 다음 단계로 넘어가기 전에
CLAUDE.md의 컨벤션과 의존성 방향 규칙을 확인한다.

### 1단계: Domain Model
- `domain/model/` 에 핵심 엔티티/값 객체 작성
- Spring 어노테이션 금지, 순수 Java
- `@Value` (Lombok) 또는 Java Record 활용

### 2단계: Domain Event (이벤트가 필요한 경우)
- `domain/event/` 에 `{명사}{과거동사}Event` 형태로 작성
- `DomainEvent` 인터페이스 구현
- `eventId(UUID)`, `occurredAt(Instant)` 포함

### 3단계: Inbound Port (UseCase)
- `application/port/in/` 에 `{동사}{명사}UseCase` 인터페이스 작성
- Command/Query는 Java Record로 정의 (같은 파일 또는 별도 파일)

### 4단계: Outbound Port
- `application/port/out/` 에 Repository / 외부서비스 인터페이스 작성
- 인터페이스명: `{명사}Repository`, `{명사}Client`

### 5단계: Application Service
- `application/service/` 에 UseCase 구현체 작성
- `@RequiredArgsConstructor`, `@Transactional` 적용
- 이벤트 발행은 Outbound Port 통해서

### 6단계: Persistence Adapter
- `adapter/out/persistence/` 에 JPA Entity + Repository 구현
- JPA Entity는 `{명사}Entity` suffix 필수
- Mapper: `{명사}Mapper` (MapStruct)

### 7단계: Web Adapter
- `adapter/in/web/` 에 Controller + Request/Response DTO 작성
- `@RestController`, `@RequestMapping("/api/v1/...")`
- 응답: `ApiResponse<T>` 래퍼 사용

### 8단계: 테스트
- Application Service 단위 테스트 (Mockito)
- Controller 슬라이스 테스트 (`@WebMvcTest`)
- 통합 테스트 (Testcontainers, `@SpringBootTest`)

## 체크리스트
- [ ] 도메인 모델에 Spring/JPA 어노테이션 없음
- [ ] Application Service가 HTTP 객체 미참조
- [ ] 모든 의존성이 Port 인터페이스 통해 주입
- [ ] `@Transactional`이 Application Service에만 있음
- [ ] ApiResponse 래퍼 사용
- [ ] 테스트 `@DisplayName` 한국어 작성
