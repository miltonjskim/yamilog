/**
 * user-service
 * ─────────────────────────────────────────────────────────────────────────────
 * 역할: 회원가입/로그인, 프로필, 팔로우/팔로워, 레벨 프로필 표시
 * DB  : PostgreSQL (Spring Data JPA)
 * 이벤트 발행: UserFollowed, UserRegistered
 * 이벤트 구독: LevelChanged
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

    // ── DB: PostgreSQL + JPA ──────────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // ── Cache: Redis ──────────────────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // ── 소셜 로그인 (OAuth2 Resource Server) ──────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

    // ── Kafka (이벤트 발행/구독) ──────────────────────────────────────────────
    implementation("org.springframework.kafka:spring-kafka")

    // ── MapStruct ─────────────────────────────────────────────────────────────
    implementation("org.mapstruct:mapstruct")
    annotationProcessor("org.mapstruct:mapstruct-processor")

    // ── Test ──────────────────────────────────────────────────────────────────
    testImplementation(testFixtures(project(":common:common-domain"))) // HexagonalArchRules + ArchUnit
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("com.h2database:h2") // 단위 테스트용 인메모리 DB
}
