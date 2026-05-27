/**
 * search-service
 * ─────────────────────────────────────────────────────────────────────────────
 * 역할: 장소·리뷰 풀텍스트 검색, 자동완성, 위치 기반 Geo 검색
 * DB  : Elasticsearch 8 (전용)
 * 이벤트 구독: PlaceCreated, PlaceUpdated, ReviewCreated, ReviewUpdated, ReviewDeleted
 * 특징: 읽기 전용 서비스, 데이터는 이벤트로 수신하여 색인
 *        Spring Data Elasticsearch 사용
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

    // ── Elasticsearch ─────────────────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")

    // ── Cache: Redis (자동완성 캐시, 인기 검색어) ─────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // ── MapStruct ─────────────────────────────────────────────────────────────
    implementation("org.mapstruct:mapstruct")
    annotationProcessor("org.mapstruct:mapstruct-processor")

    // ── Test ──────────────────────────────────────────────────────────────────
    testImplementation("org.testcontainers:elasticsearch")
    testImplementation("org.testcontainers:kafka")
}
