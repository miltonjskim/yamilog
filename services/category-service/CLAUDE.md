# category-service — 서비스 컨텍스트

> 루트 CLAUDE.md와 함께 읽을 것.

## 역할
카테고리 메타데이터와 카테고리별 평가 항목 스키마 제공. 읽기 빈도 매우 높음.

## DB / 포트
- **DB**: PostgreSQL (`yamilog_category`) + Redis (스키마 캐시)
- **Port**: 8082

## Kafka 이벤트
- **발행**: `yamilog.category.schema-updated` (CategorySchemaUpdatedEvent)
- **구독**: 없음

## 패키지 루트
`com.yamilog.categoryservice`

## 핵심 도메인 모델
- `Category`: categoryId, name, description, iconUrl, isActive
- `EvaluationField`: fieldKey, displayName, fieldType, options, required, sortOrder

## JPA 테이블
- `categories` — 카테고리 목록
- `evaluation_fields` — 카테고리별 평가 항목

## 캐시 전략
스키마는 자주 바뀌지 않으므로 Redis TTL 1시간.
스키마 변경 시 `CategorySchemaUpdatedEvent` 발행 → 구독 서비스가 캐시 무효화.

## 주의사항
- 카테고리 스키마 변경은 관리자 전용 API. `@RequireRole(ADMIN)` 필수.
- review-service가 이 서비스를 동기 호출(WebClient)함. 응답 지연 최소화.
