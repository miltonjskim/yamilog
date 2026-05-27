현재 작업 중인 코드의 헥사고날 아키텍처 원칙 준수 여부를 검토한다.

## 사용법
/review-arch [파일경로 또는 서비스명]
예: /review-arch services/review-service
    /review-arch services/user-service/src/main/java/com/yamilog/userservice/application/service/CreateUserService.java

## 검토 항목

### ✅ 의존성 방향 검사
- [ ] `domain/` 패키지가 다른 패키지를 import하지 않는가?
- [ ] `application/` 패키지가 `adapter/` 패키지를 import하지 않는가?
- [ ] `adapter/` 패키지가 Port 인터페이스만 호출하는가? (구현체 직접 참조 없음)

### ✅ 도메인 순수성 검사
- [ ] `domain/model/` 클래스에 `@Entity`, `@Document` 없는가?
- [ ] `domain/model/` 클래스에 Spring 어노테이션 없는가?
- [ ] `domain/event/` 클래스가 `DomainEvent` 인터페이스 구현하는가?

### ✅ Application Service 검사
- [ ] `@Transactional` 이 Service 메서드에만 선언되어 있는가?
- [ ] `HttpServletRequest`, `HttpServletResponse` 등 HTTP 객체 미사용?
- [ ] UseCase 인터페이스를 구현하는가?
- [ ] Outbound Port만 의존하는가? (Adapter 구현체 직접 참조 없음)

### ✅ Controller 검사
- [ ] `ApiResponse<T>` 래퍼를 반환하는가?
- [ ] `/api/v1/` 접두사가 있는가?
- [ ] Request/Response DTO가 도메인 모델을 직접 노출하지 않는가?
- [ ] UseCase 인터페이스만 호출하는가?

### ✅ 네이밍 규칙 검사
- [ ] UseCase 인터페이스: `{동사}{명사}UseCase`
- [ ] Command/Query: Java Record 사용
- [ ] JPA Entity: `{명사}Entity` suffix
- [ ] Mongo Document: `{명사}Document` suffix
- [ ] 이벤트: `{명사}{과거동사}Event`

### ✅ 테스트 검사
- [ ] Domain 테스트: 외부 의존성 없는 순수 단위 테스트
- [ ] Service 테스트: Mockito로 Port 목킹
- [ ] `@DisplayName` 한국어 작성

## 출력 형식
문제 발견 시:
```
❌ [위반 유형] 파일명:라인번호
   문제: 설명
   해결: 권장 코드
```

문제 없을 시:
```
✅ 헥사고날 아키텍처 원칙 준수 확인됨
```
