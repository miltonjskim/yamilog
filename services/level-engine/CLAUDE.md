# level-engine — 서비스 컨텍스트

> 루트 CLAUDE.md와 함께 읽을 것.

## 역할
리뷰 수·퀄리티 기반 매니아 등급 자동 산정. 실시간 이벤트 처리 + 일 배치 병행.

## DB / 포트
- **DB**: PostgreSQL (`yamilog_level`) + Redis (실시간 카운터)
- **Port**: 8086

## Kafka 이벤트
- **발행**: `yamilog.level.changed` (LevelChangedEvent)
- **구독**: `yamilog.review.created`, `yamilog.review.updated`

## 패키지 루트
`com.yamilog.levelengine`

## 퀄리티 점수 계산 로직
```
qualityScore = (contentLengthScore * 0.30)
             + (completenessScore  * 0.30)
             + (helpfulVoteScore   * 0.40)

contentLengthScore  : 글자 수 기반 0-100 (300자 이상 = 100점)
completenessScore   : 필수 항목 작성 비율 0-100
helpfulVoteScore    : helpful / (helpful + notHelpful) * 100
```

## 레벨 산정 트리거
1. **실시간**: `ReviewCreatedEvent` / `ReviewUpdatedEvent` 수신 시
   - Redis에서 리뷰 수, 평균 퀄리티 점수 갱신
   - 임계값 초과 여부 확인 → 변경 시 `LevelChangedEvent` 발행
2. **배치**: 매일 새벽 3시 (`Spring Batch Job`)
   - 전체 사용자 퀄리티 점수 재계산 (Redis 카운터와 DB 동기화)
   - 미활동 강등 처리

## JPA 테이블
- `level_histories` — 레벨 변경 이력 (감사 로그)
- `level_rules` — 레벨별 임계값 (DB 관리, 코드 하드코딩 금지)

## 주의사항
- 레벨 계산은 이 서비스만 담당. user-service가 직접 계산 금지.
- Redis 카운터가 유일한 실시간 소스. DB는 배치 동기화 대상.
- 동일 리뷰로 중복 레벨 계산 방지: `eventId` 기반 멱등성 처리 필수.
