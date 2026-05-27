/**
 * place-service
 * ─────────────────────────────────────────────────────────────────────────────
 * 역할: 장소 등록, 장소 검색, 카카오/네이버 지도 API 연동, 광고 상단 노출 연계
 * DB  : PostgreSQL (PostGIS 확장으로 위치 데이터 처리)
 * 이벤트 발행: PlaceCreated, PlaceUpdated
 * 외부 API : Kakao Local API, Naver Geocoding API
 */
plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // ── 내부 모듈 ─────────────────────────────────────────────────────────────
    implementation(project(":common:common-domain"))
    implementation(project(":common:common-infra"))

    // ── Web ───────────────────────────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // ── DB: PostgreSQL + JPA (PostGIS 위치 데이터) ────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")
    // Hibernate Spatial: PostGIS 지원
    implementation("org.hibernate.orm:hibernate-spatial")
    // JTS Topology: 위경도 Point 타입 처리
    implementation("org.locationtech.jts:jts-core:1.19.0")

    // ── Cache: Redis ──────────────────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // ── 외부 API 클라이언트 (Kakao, Naver 지도) ───────────────────────────────
    // WebClient (비동기 외부 API 호출)
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Resilience4j (외부 API 장애 대응)
    implementation("io.github.resilience4j:resilience4j-spring-boot3")
    implementation("io.github.resilience4j:resilience4j-reactor:2.2.0")

    // ── MapStruct ─────────────────────────────────────────────────────────────
    implementation("org.mapstruct:mapstruct")
    annotationProcessor("org.mapstruct:mapstruct-processor")

    // ── Test ──────────────────────────────────────────────────────────────────
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:kafka")
    testImplementation("io.projectreactor:reactor-test")
    // WireMock: 카카오/네이버 외부 API 목킹
    testImplementation("org.wiremock:wiremock-standalone:3.9.1")
}
