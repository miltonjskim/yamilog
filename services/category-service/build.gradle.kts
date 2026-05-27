/**
 * category-service
 * ─────────────────────────────────────────────────────────────────────────────
 * 역할: 카테고리 메타데이터 관리, 카테고리별 평가 항목(스키마) 제공
 * DB  : PostgreSQL (Spring Data JPA)
 * 특징: 읽기 빈도 매우 높음 → Redis 캐시 필수, 쓰기 빈도 낮음 (관리자만)
 * 이벤트 발행: CategorySchemaUpdated
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

    // ── Cache: Redis (스키마 캐시) ────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // ── MapStruct ─────────────────────────────────────────────────────────────
    implementation("org.mapstruct:mapstruct")
    annotationProcessor("org.mapstruct:mapstruct-processor")

    // ── Test ──────────────────────────────────────────────────────────────────
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.h2database:h2")
}
