Controller를 분석하여 OpenAPI 3.0 스펙을 생성한다.

## 사용법
/gen-api-spec [서비스명 또는 Controller 파일경로]
예: /gen-api-spec review-service
    /gen-api-spec services/review-service/src/main/java/.../ReviewController.java

## 생성 내용
- `docs/specs/api/{서비스명}-api.yaml` — OpenAPI 3.0 YAML
- 각 엔드포인트의 request/response 스키마
- 에러 응답 (ApiResponse 래퍼 포함)
- 인증 헤더 (Authorization: Bearer)

## springdoc 어노테이션 추가
Controller에 다음 어노테이션을 추가한다:
- `@Tag(name = "...")` — 컨트롤러 그룹명
- `@Operation(summary = "...")` — 각 메서드 설명
- `@ApiResponse` — 응답 코드별 설명

## build.gradle.kts에 springdoc 추가
```
implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")
```
Swagger UI: http://localhost:{port}/swagger-ui.html
