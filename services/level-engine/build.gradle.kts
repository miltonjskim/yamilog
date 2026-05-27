/**
 * level-engine
 * ─────────────────────────────────────────────────────────────────────────────
 * 역할: 매니아 등급 자동 산정, 퀄리티 점수 계산, 레벨 이력 관리, 일배치
 * DB  : PostgreSQL (레벨 이력, 규칙 관리) + Redis (실시간 집계 카운터)
 * 이벤트 구독: ReviewCreated, ReviewUpdated
 * 이벤트 발행: LevelChanged
 * 특징: 이벤트 기반 실시간 처리 + Spring Batch 일별 배치 병행
 */
plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // ── 내부 모듈 ─────────────────────────────────────────────────────────────
    implementation(project(":common:common-domain"))
    implementation(project(":common:common-infra"))

    // ── Web (관리 API, 레벨 조회 API) ────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // ── DB: PostgreSQL (레벨 규칙, 레벨 이력) ────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // ── Redis (실시간 리뷰 카운터, 유용성 투표 집계) ───────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // ── Spring Batch (일별 전체 레벨 재산정 배치) ─────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-batch")

    // ── MapStruct ─────────────────────────────────────────────────────────────
    implementation("org.mapstruct:mapstruct")
    annotationProcessor("org.mapstruct:mapstruct-processor")

    // ── Test ──────────────────────────────────────────────────────────────────
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.springframework.batch:spring-batch-test")
    testImplementation("com.h2database:h2")
}
