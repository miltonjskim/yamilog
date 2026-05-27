/**
 * feed-service
 * ─────────────────────────────────────────────────────────────────────────────
 * 역할: 팔로우 피드, 카테고리 피드, 매니아 추천 픽, 위치 기반 피드
 * DB  : MongoDB (피드 도큐먼트 저장)
 * 이벤트 구독: ReviewCreated, ReviewUpdated, LevelChanged, PlaceCreated, UserFollowed
 * 특징: 쓰기는 이벤트 기반(비동기), 읽기 성능이 핵심 → Redis 캐시 + MongoDB
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

    // ── DB: MongoDB (피드 도큐먼트) ───────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // ── Cache: Redis (피드 캐시, 인기 피드 TTL 관리) ──────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // ── MapStruct ─────────────────────────────────────────────────────────────
    implementation("org.mapstruct:mapstruct")
    annotationProcessor("org.mapstruct:mapstruct-processor")

    // ── Test ──────────────────────────────────────────────────────────────────
    testImplementation("org.testcontainers:mongodb")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.testcontainers:toxiproxy") // 네트워크 지연 테스트
}
