# ad-service — 서비스 컨텍스트

> 루트 CLAUDE.md와 함께 읽을 것.

## 역할
업장 광고주 등록, 검색 결과 상단 노출 슬롯 관리, 노출 카운터 집계.

## DB / 포트
- **DB**: PostgreSQL (`yamilog_ad`) + Redis (실시간 노출 카운터)
- **Port**: 8088

## Kafka 이벤트
- **발행**: `yamilog.ad.impression` (AdImpressionEvent, 비동기 로깅용)
- **구독**: 없음

## 패키지 루트
`com.yamilog.adservice`

## 핵심 도메인 모델
- `AdCampaign`: campaignId, placeId, advertiserId, categoryId, regionCode, startAt, endAt, budget
- `AdSlot`: slotId, campaignId, position(1 or 2), isActive

## JPA 테이블
- `ad_campaigns` — 광고 캠페인
- `ad_slots` — 상단 노출 슬롯 예약
- `ad_impression_logs` — 노출 이력 (파티셔닝 고려)

## 슬롯 조회 API (place-service가 동기 호출)
```
GET /api/v1/ads/slots?categoryId={id}&regionCode={code}
→ 최대 2개 슬롯 반환 (isActive=true, 현재 시각이 startAt~endAt 범위)
```

## 주의사항
- 슬롯은 지역(regionCode) + 카테고리 조합으로 관리.
- 노출 카운터는 Redis INCR. 일 배치로 DB 집계.
- `광고` 뱃지 표시 여부 결정은 **프론트엔드** 담당 (API 응답에 `isAd: true` 포함).
