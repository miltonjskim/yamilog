# place-service — 서비스 컨텍스트

> 루트 CLAUDE.md와 함께 읽을 것.

## 역할
장소 등록, 위치 기반 검색, 카카오/네이버 지도 API 연동, 검색 결과 광고 슬롯 연계.

## DB / 포트
- **DB**: PostgreSQL + PostGIS (`yamilog_place`) + Redis
- **Port**: 8083

## Kafka 이벤트
- **발행**: `yamilog.place.created`, `yamilog.place.updated`
- **구독**: 없음

## 패키지 루트
`com.yamilog.placeservice`

## 핵심 도메인 모델
- `Place`: placeId, name, categoryId, address, location(Point), businessHours, ownerId
- `PlaceRatingSummary`: placeId, categoryId, levelGroup별 평균점수, 리뷰 수

## JPA 테이블 (PostGIS)
- `places` — location 컬럼: `GEOGRAPHY(POINT, 4326)`
- `place_rating_summaries` — 레벨 그룹별 평균 점수 집계

## 위치 검색 쿼리 패턴
```java
// 반경 검색: ST_DWithin 사용 (PostGIS)
@Query("SELECT p FROM PlaceEntity p WHERE ST_DWithin(p.location, :point, :radiusMeters) = true")
```

## 외부 API
- Kakao Local API: 주소 → 좌표 변환, 장소명 검색
- Naver Geocoding: 대안 주소 변환
- 두 API 모두 Resilience4j CircuitBreaker 적용 필수

## 주의사항
- 장소 등록: `ManiaLevel.ENTHUSIAST` 이상만 가능
- 반경 50m 이내 동일 카테고리 중복 등록 시 경고 (금지는 아님)
- ad-service를 동기 호출해 검색 결과 상단 슬롯 2개 조회
