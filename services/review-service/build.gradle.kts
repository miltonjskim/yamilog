/**
 * review-service
 * ─────────────────────────────────────────────────────────────────────────────
 * 역할: 리뷰 CRUD, 카테고리별 평가 항목 저장, 유용성 투표, 신고
 * DB  : MongoDB (카테고리별 동적 스키마 지원)
 * 이벤트 발행: ReviewCreated, ReviewUpdated, ReviewDeleted, ReviewReported
 * 이벤트 구독: CategorySchemaUpdated (스키마 캐시 갱신)
 * 특징: 카테고리마다 평가 항목이 다름 → MongoDB Document 구조 최적
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

    // ── DB: MongoDB ───────────────────────────────────────────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // ── Cache: Redis (리뷰 집계 캐시, 장소별 평균 점수) ──────────────────────
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    // ── 이미지 업로드 (S3 Multipart) ──────────────────────────────────────────
    implementation("software.amazon.awssdk:s3:2.28.17")
    implementation("software.amazon.awssdk:sts:2.28.17") // IAM Role 지원

    // ── MapStruct ─────────────────────────────────────────────────────────────
    implementation("org.mapstruct:mapstruct")
    annotationProcessor("org.mapstruct:mapstruct-processor")

    // ── Test ──────────────────────────────────────────────────────────────────
    testImplementation("org.testcontainers:mongodb")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.testcontainers:localstack") // S3 목킹
}
