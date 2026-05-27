/**
 * ad-service
 * ─────────────────────────────────────────────────────────────────────────────
 * 역할: 업장 광고주 등록, 상단 노출 슬롯 관리, 노출 로그 집계
 * DB  : PostgreSQL (광고 캠페인, 슬롯 예약) + Redis (실시간 노출 카운터)
 * 이벤트 발행: AdImpressionLogged (비동기 로깅)
 * 특징: 검색 결과 상단 노출 슬롯은 place-service 가 ad-service 를 동기 조회
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

    // ── DB: PostgreSQL ────────────────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // ── Redis (실시간 노출 카운터, 슬롯 락) ──────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // ── MapStruct ─────────────────────────────────────────────────────────────
    implementation("org.mapstruct:mapstruct")
    annotationProcessor("org.mapstruct:mapstruct-processor")

    // ── Test ──────────────────────────────────────────────────────────────────
    testImplementation("org.testcontainers:postgresql")
    testImplementation("org.testcontainers:kafka")
    testImplementation("com.h2database:h2")
}
