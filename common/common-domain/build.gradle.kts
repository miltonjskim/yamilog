/**
 * common-domain
 * ─────────────────────────────────────────────────────────────────────────────
 * 역할: 공유 도메인 모델 — VO, 도메인 이벤트 인터페이스, 예외 계층
 * 제약: Spring / JPA / Kafka 의존 금지 (순수 Java 라이브러리)
 *
 * 루트 build.gradle.kts 에서 java, dependency-management, Lombok 전역 적용됨
 */

// 추가 의존성 없음 — 루트 공통 설정(Lombok, JUnit 5)만 상속
