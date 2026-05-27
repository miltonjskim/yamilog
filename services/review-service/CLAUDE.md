# review-service — 서비스 컨텍스트

> 루트 CLAUDE.md와 함께 읽을 것.

## 역할
리뷰 CRUD, 카테고리별 동적 평가 항목 저장, 유용성 투표, 신고 처리.

## DB / 포트
- **DB**: MongoDB (`yamilog_review`) + Redis (집계 캐시)
- **Port**: 8084

## Kafka 이벤트
- **발행**: `yamilog.review.created`, `yamilog.review.updated`, `yamilog.review.deleted`
- **구독**: `yamilog.category.schema-updated` (스키마 캐시 갱신)

## 패키지 루트
`com.yamilog.reviewservice`

## MongoDB Document 구조
```java
@Document(collection = "reviews")
public class ReviewDocument {
    @Id private String id;
    private String userId;
    private String placeId;
    private String categoryId;
    private int score;                          // 1-10 종합 점수
    private String content;
    private Map<String, Object> evaluationData; // 카테고리별 동적 항목
    private List<String> imageUrls;             // S3 URL
    private VisibilityLevel visibilityLevel;
    private int helpfulCount;
    private int notHelpfulCount;
    private LocalDate visitedDate;
    private Instant createdAt;
    private Instant updatedAt;
}
```

## 리뷰 작성 흐름
1. category-service WebClient 호출 → 스키마 조회
2. `evaluationData` 유효성 검증 (필수 항목, 타입 체크)
3. 이미지 S3 업로드 (비동기)
4. MongoDB 저장
5. `ReviewCreatedEvent` Kafka 발행

## 가시성 필터링
조회 시 `VisibilityLevel.isAccessibleBy(currentUserLevel)` 필터링.
Application Service에서 처리. Controller에서 직접 필터링 금지.

## 신고 처리
신고 5건 누적 시 자동 블라인드. `reportCount >= 5 → isBlinded = true`.
