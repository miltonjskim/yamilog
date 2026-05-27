/**
 * gateway
 * ─────────────────────────────────────────────────────────────────────────────
 * Spring Cloud Gateway 기반 API 게이트웨이.
 *
 * 역할:
 *   - 라우팅 (모든 /api/** 요청을 해당 서비스로 전달)
 *   - JWT 인증 필터 (GatewayFilter)
 *   - Rate Limiting (Redis 기반 RequestRateLimiter)
 *   - CORS 처리
 *   - Circuit Breaker (Resilience4j)
 *
 * ※ WebFlux 기반 — spring-boot-starter-web 과 공존 불가
 *    common-infra 의 spring-boot-starter-web 을 exclude 처리
 */
plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    // ── 내부 모듈 ─────────────────────────────────────────────────────────────
    implementation(project(":common:common-domain")) {
        // common-infra 의 spring-boot-starter-web (Servlet) 은 WebFlux 와 충돌
        // common-domain 만 직접 참조
    }

    // ── Spring Cloud Gateway (WebFlux 기반) ───────────────────────────────────
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")

    // ── 인증 ──────────────────────────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api")
    runtimeOnly("io.jsonwebtoken:jjwt-impl")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson")

    // ── Rate Limiting ─────────────────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")

    // ── Circuit Breaker ───────────────────────────────────────────────────────
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")

    // ── Observability ─────────────────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("io.micrometer:micrometer-registry-prometheus")

    // ── Validation ────────────────────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // ── Test ──────────────────────────────────────────────────────────────────
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
}
