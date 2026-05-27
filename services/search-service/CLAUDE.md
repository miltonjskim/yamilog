# search-service — 서비스 컨텍스트

> 루트 CLAUDE.md와 함께 읽을 것.

## 역할
장소·리뷰 풀텍스트 검색, 자동완성, 위치 기반 Geo 검색. 읽기 전용 서비스.

## DB / 포트
- **DB**: Elasticsearch 8 + Redis (자동완성 캐시)
- **Port**: 8087

## Kafka 이벤트
- **발행**: 없음
- **구독**: `yamilog.place.created`, `yamilog.place.updated`, `yamilog.review.created`, `yamilog.review.updated`, `yamilog.review.deleted`

## 패키지 루트
`com.yamilog.searchservice`

## Elasticsearch 인덱스

**yamilog-places**
```json
{
  "placeId": "keyword",
  "name": "text (nori 형태소 분석기)",
  "categoryId": "keyword",
  "address": "text",
  "location": "geo_point",
  "maniaAvgScore": "float",
  "reviewCount": "integer"
}
```

**yamilog-reviews**
```json
{
  "reviewId": "keyword",
  "placeId": "keyword",
  "categoryId": "keyword",
  "content": "text (nori)",
  "visibilityLevel": "integer",
  "score": "integer",
  "createdAt": "date"
}
```

## 검색 쿼리 패턴
- 키워드 검색: `multi_match` (name, address, content)
- Geo 검색: `geo_distance` 필터
- 자동완성: `completion` 필드 또는 `prefix` 쿼리 + Redis 캐시

## 주의사항
- 이 서비스는 **쓰기 API 없음**. 모든 데이터는 Kafka 이벤트로만 수신.
- 리뷰 검색 결과에 `visibilityLevel` 필터 적용 필수 (접근자 레벨 기준).
- nori 형태소 분석기 설치 필요: `elasticsearch-analysis-nori` 플러그인.
