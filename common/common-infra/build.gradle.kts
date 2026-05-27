/**
 * common-infra
 * ─────────────────────────────────────────────────────────────────────────────
 * 역할: Kafka 설정, JWT, Security 필터, ApiResponse, GlobalExceptionHandler
 * 모든 마이크로서비스가 이 모듈에 의존
 *
 * common-domain 을 api() 로 노출 → 서비스 모듈에서 common-domain 별도 선언 불필요
 * java-library 적용: api() 설정 사용 가능
 */
plugins {
    `java-library`
}

dependencies {
    // ── 내부 모듈 (api: 상위 모듈로 전이 노출) ─────────────────────────────────
    api(project(":common:common-domain"))

    // ── Web ───────────────────────────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // ── Security + AOP (@RequireLevel 어노테이션 처리) ─────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-aop")

    // ── JWT ───────────────────────────────────────────────────────────────────
    implementation("io.jsonwebtoken:jjwt-api")
    runtimeOnly("io.jsonwebtoken:jjwt-impl")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson")

    // ── Kafka ─────────────────────────────────────────────────────────────────
    implementation("org.springframework.kafka:spring-kafka")

    // ── JSON ──────────────────────────────────────────────────────────────────
    implementation("com.fasterxml.jackson.core:jackson-databind")

    // ── Test ──────────────────────────────────────────────────────────────────
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:kafka")
}
