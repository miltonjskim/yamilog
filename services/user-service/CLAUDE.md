# user-service — 서비스 컨텍스트

> 루트 CLAUDE.md와 함께 읽을 것.

## 역할
회원가입/로그인, 프로필 관리, 팔로우/팔로워, 카테고리별 레벨 프로필 표시.

## DB / 포트
- **DB**: PostgreSQL (`yamilog_user`)
- **Port**: 8081

## Kafka 이벤트
- **발행**: `yamilog.user.followed` (UserFollowedEvent)
- **구독**: `yamilog.level.changed` (LevelChangedEvent → 프로필 레벨 갱신)

## 패키지 루트
`com.yamilog.userservice`

## 핵심 도메인 모델
- `User`: userId, nickname, email, profileImage, followersCount, followingCount
- `UserLevel`: userId, categoryId, maniaLevel, qualityScore, reviewCount
- `Follow`: followerId, followeeId, createdAt

## JPA 테이블
- `users` — 회원 기본 정보
- `user_levels` — 카테고리별 레벨 (userId + categoryId 복합 유니크)
- `follows` — 팔로우 관계 (followerId + followeeId 복합 PK)

## 소셜 로그인
OAuth2 Resource Server 방식. Google/Kakao ID Token 검증 후 자체 JWT 발급.
소셜 제공자별 사용자 식별: `provider_type + provider_id` 조합으로 유니크.

## 주의사항
- `UserLevel`은 level-engine이 Kafka로 업데이트. user-service가 직접 레벨 계산 금지.
- 팔로우 수는 Redis 카운터 + 주기적 DB 동기화 방식.
