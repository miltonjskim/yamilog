현재 파일을 분석하여 테스트 코드를 자동 생성한다.

## 사용법
/gen-test [파일경로]
예: /gen-test services/review-service/src/main/java/com/yamilog/reviewservice/application/service/CreateReviewService.java

## 계층별 생성 전략

### Domain Model → 단위 테스트
```java
// 외부 의존성 0, 순수 로직 검증
class ReviewTest {
    @Test
    @DisplayName("종합 점수가 1-10 범위를 벗어나면 예외가 발생한다")
    void score_outOfRange_throwsException() { ... }
}
```

### Application Service → Mockito 단위 테스트
```java
@ExtendWith(MockitoExtension.class)
class CreateReviewServiceTest {
    @InjectMocks CreateReviewService sut;
    @Mock ReviewRepository reviewRepository;
    @Mock ReviewEventPublisher eventPublisher;

    @Test
    @DisplayName("리뷰 생성 시 ReviewCreatedEvent가 발행된다")
    void createReview_publishesEvent() { ... }
}
```

### Controller → @WebMvcTest 슬라이스 테스트
```java
@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    @Autowired MockMvc mockMvc;
    @MockBean CreateReviewUseCase createReviewUseCase;

    @Test
    @DisplayName("POST /api/v1/reviews 요청 시 201을 반환한다")
    void createReview_returns201() { ... }
}
```

### JPA Repository → @DataJpaTest
```java
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
@Testcontainers
class ReviewRepositoryAdapterTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");
    ...
}
```

### Kafka Listener → 통합 테스트
```java
@SpringBootTest
@EmbeddedKafka(topics = {"yamilog.review.created"})
class ReviewCreatedEventListenerTest { ... }
```

## 테스트 생성 규칙
1. 메서드명: `{조건}_{기대결과}` (camelCase)
2. `@DisplayName`: 한국어, 구체적인 시나리오 설명
3. Given-When-Then 주석 구조
4. Happy Path + Edge Case + 예외 케이스 포함
5. 픽스처 메서드: `private {명사} create{명사}(...)` 형태
