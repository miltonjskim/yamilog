package com.yamilog.userservice.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.yamilog.common.test.HexagonalArchRules;

/**
 * user-service 헥사고날 아키텍처 의존성 규칙 검증.
 *
 * 새 서비스 추가 시: ROOT 패키지만 바꿔서 동일하게 작성할 것.
 * 규칙 정의는 HexagonalArchRules (common-domain testFixtures) 에서 관리.
 */
@AnalyzeClasses(packages = HexagonalArchitectureTest.ROOT)
class HexagonalArchitectureTest {

    static final String ROOT = "com.yamilog.userservice";

    // ── 레이어 간 의존성 방향 ─────────────────────────────────────────────────

    @ArchTest
    static final ArchRule domainNotDependOnAdapter =
            HexagonalArchRules.domainShouldNotDependOnAdapter(ROOT);

    @ArchTest
    static final ArchRule domainNotDependOnApplication =
            HexagonalArchRules.domainShouldNotDependOnApplication(ROOT);

    @ArchTest
    static final ArchRule applicationNotDependOnAdapter =
            HexagonalArchRules.applicationShouldNotDependOnAdapter(ROOT);

    @ArchTest
    static final ArchRule inboundNotDependOnOutbound =
            HexagonalArchRules.inboundAdapterShouldNotDependOnOutboundAdapter(ROOT);

    // ── 프레임워크 오염 방지 ──────────────────────────────────────────────────

    @ArchTest
    static final ArchRule domainModelNoSpring =
            HexagonalArchRules.domainModelShouldNotUseSpringAnnotations(ROOT);

    @ArchTest
    static final ArchRule domainModelNoJpa =
            HexagonalArchRules.domainModelShouldNotUseJpaAnnotations(ROOT);

    @ArchTest
    static final ArchRule applicationNoServlet =
            HexagonalArchRules.applicationServiceShouldNotUseServletClasses(ROOT);
}
