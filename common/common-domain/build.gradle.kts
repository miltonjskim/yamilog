/**
 * common-domain
 * ─────────────────────────────────────────────────────────────────────────────
 * 역할: 공유 도메인 모델 — VO, 도메인 이벤트 인터페이스, 예외 계층
 * 제약: Spring / JPA / Kafka 의존 금지 (순수 Java 라이브러리)
 *
 * 루트 build.gradle.kts 에서 java, dependency-management, Lombok 전역 적용됨
 */
plugins {
    `java-test-fixtures`
}

dependencies {
    // testFixtures 소비 모듈이 archunit 을 별도 선언하지 않아도 되도록 api 로 노출
    testFixturesApi("com.tngtech.archunit:archunit-junit5")

    testFixturesCompileOnly("org.projectlombok:lombok")
    testFixturesAnnotationProcessor("org.projectlombok:lombok")
}
