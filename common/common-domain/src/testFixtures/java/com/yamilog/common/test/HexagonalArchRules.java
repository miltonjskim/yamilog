package com.yamilog.common.test;

import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * 모든 마이크로서비스의 헥사고날 아키텍처 의존성 규칙 모음.
 * 각 서비스의 HexagonalArchitectureTest 에서 패키지 루트를 주입하여 사용한다.
 *
 * <pre>
 * 의존성 방향 규칙 (위반 시 컴파일 타임 테스트 실패):
 *   adapter/in  →  application/port/in  →  application/service
 *                                                 ↓
 *   adapter/out  ←  application/port/out  ←  (구현)
 *
 *   domain 은 어떤 레이어도 의존하지 않는다.
 * </pre>
 */
public final class HexagonalArchRules {

    private HexagonalArchRules() {}

    // ── 레이어 간 의존성 방향 ───────────────────────────────────────────────────

    public static ArchRule domainShouldNotDependOnAdapter(String basePackage) {
        return noClasses()
                .that().resideInAPackage(pkg(basePackage, "domain.."))
                .should().dependOnClassesThat().resideInAPackage(pkg(basePackage, "adapter.."))
                .as("[아키텍처] domain 은 adapter 에 의존할 수 없다");
    }

    public static ArchRule domainShouldNotDependOnApplication(String basePackage) {
        return noClasses()
                .that().resideInAPackage(pkg(basePackage, "domain.."))
                .should().dependOnClassesThat().resideInAPackage(pkg(basePackage, "application.."))
                .as("[아키텍처] domain 은 application 에 의존할 수 없다");
    }

    public static ArchRule applicationShouldNotDependOnAdapter(String basePackage) {
        return noClasses()
                .that().resideInAPackage(pkg(basePackage, "application.."))
                .should().dependOnClassesThat().resideInAPackage(pkg(basePackage, "adapter.."))
                .as("[아키텍처] application 은 adapter 에 의존할 수 없다");
    }

    public static ArchRule inboundAdapterShouldNotDependOnOutboundAdapter(String basePackage) {
        return noClasses()
                .that().resideInAPackage(pkg(basePackage, "adapter.in.."))
                .should().dependOnClassesThat().resideInAPackage(pkg(basePackage, "adapter.out.."))
                .as("[아키텍처] adapter.in 은 adapter.out 에 직접 의존할 수 없다 (port 인터페이스를 통할 것)");
    }

    // ── 프레임워크 오염 방지 ────────────────────────────────────────────────────

    public static ArchRule domainModelShouldNotUseSpringAnnotations(String basePackage) {
        return noClasses()
                .that().resideInAPackage(pkg(basePackage, "domain.model.."))
                .should().dependOnClassesThat().resideInAPackage("org.springframework..")
                .as("[아키텍처] domain.model 은 Spring 어노테이션에 의존할 수 없다");
    }

    public static ArchRule domainModelShouldNotUseJpaAnnotations(String basePackage) {
        return noClasses()
                .that().resideInAPackage(pkg(basePackage, "domain.model.."))
                .should().dependOnClassesThat().resideInAPackage("jakarta.persistence..")
                .as("[아키텍처] domain.model 은 JPA 어노테이션에 의존할 수 없다 (*Entity 는 adapter.out.persistence 에만)");
    }

    public static ArchRule applicationServiceShouldNotUseServletClasses(String basePackage) {
        return noClasses()
                .that().resideInAPackage(pkg(basePackage, "application.."))
                .should().dependOnClassesThat().resideInAPackage("jakarta.servlet..")
                .as("[아키텍처] application 계층은 Servlet API(HTTP)에 의존할 수 없다");
    }

    // ── 헬퍼 ────────────────────────────────────────────────────────────────────

    private static String pkg(String base, String suffix) {
        return base + "." + suffix;
    }
}
