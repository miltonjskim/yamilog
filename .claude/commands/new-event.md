Kafka 도메인 이벤트 발행/구독 코드를 생성한다.

## 사용법
/new-event [이벤트명] [발행서비스] [구독서비스들]
예: /new-event ReviewCreated review-service level-engine,feed-service,search-service

## 생성 파일 목록

### 발행 서비스 측 (publisher)

**1. 이벤트 클래스** — `common-domain`의 `domain/event/`
```
{이벤트명}Event.java
```
포함 필드:
- `String eventId` — UUID.randomUUID().toString()
- `String eventType` — 이벤트명 상수
- `Instant occurredAt`
- 도메인 페이로드 필드들

**2. Outbound Port** — `application/port/out/`
```
{이벤트명}EventPublisher.java  (인터페이스)
```

**3. Messaging Adapter** — `adapter/out/messaging/`
```
Kafka{이벤트명}EventPublisher.java  (구현체)
```
- `KafkaTemplate<String, Object>` 사용
- 토픽: `yamilog.{도메인}.{이벤트소문자}`
- 키: aggregateId

### 구독 서비스 측 (각 구독 서비스마다)

**4. Event Listener** — `adapter/in/messaging/`
```
{이벤트명}EventListener.java
```
- `@KafkaListener(topics = "yamilog.{도메인}.{이벤트소문자}", groupId = "{서비스명}")`
- 역직렬화 후 UseCase 호출

## Kafka 토픽 네이밍 규칙
```
yamilog.{도메인소문자}.{이벤트소문자}
예) yamilog.review.created
    yamilog.level.changed
    yamilog.place.created
```

## 멱등성 처리
구독 서비스는 반드시 `eventId` 기반 중복 처리 방어 로직 포함:
```java
if (processedEventRepository.exists(event.eventId())) return;
```

## 체크리스트
- [ ] 이벤트 클래스 common-domain에 위치
- [ ] Publisher는 Port 인터페이스로 추상화
- [ ] Listener에 `@KafkaListener` groupId 명시
- [ ] 멱등성 처리 포함
- [ ] 토픽명이 네이밍 규칙 준수
