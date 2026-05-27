# feed-service — 서비스 컨텍스트

> 루트 CLAUDE.md와 함께 읽을 것.

## 역할
팔로우 피드, 카테고리 피드, 매니아 추천 픽 제공. 쓰기는 이벤트 기반, 읽기 성능이 핵심.

## DB / 포트
- **DB**: MongoDB (`yamilog_feed`) + Redis (피드 캐시 5분 TTL)
- **Port**: 8085

## Kafka 이벤트
- **발행**: 없음
- **구독**: `yamilog.review.created`, `yamilog.review.updated`, `yamilog.level.changed`, `yamilog.place.created`, `yamilog.user.followed`

## 패키지 루트
`com.yamilog.feedservice`

## MongoDB Document 구조
```java
@Document(collection = "feed_items")
public class FeedItemDocument {
    @Id private String id;
    private FeedType feedType;        // FOLLOW / CATEGORY / MANIA_PICK / NEARBY
    private String targetUserId;      // 이 피드를 보는 사용자 (FOLLOW 타입)
    private String categoryId;        // CATEGORY 타입
    private String reviewId;
    private String authorId;
    private String placeId;
    private double score;             // 정렬 점수 (유용성 투표 기반)
    private Instant createdAt;        // TTL 인덱스: 30일 자동 삭제
}
```

## 피드 조회 전략
- **팔로우 피드**: `targetUserId` + `createdAt DESC` 커서 페이지네이션
- **카테고리 피드**: `categoryId` + `score DESC` (최근 7일 유용성 투표 집계)
- **매니아 추천 픽**: Expert(3) 이상 작성자 + score 상위

## 주의사항
- 피드 **쓰기**는 Kafka Consumer에서만 발생. API로 직접 쓰기 금지.
- 30일 TTL 인덱스로 오래된 피드 자동 삭제.
- 캐시 무효화: 새 이벤트 수신 시 해당 사용자/카테고리 캐시 삭제.
