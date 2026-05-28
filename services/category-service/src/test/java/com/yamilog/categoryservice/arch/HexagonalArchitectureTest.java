package com.yamilog.categoryservice.arch;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.yamilog.common.test.HexagonalArchRules;

@AnalyzeClasses(packages = HexagonalArchitectureTest.ROOT)
class HexagonalArchitectureTest {

    static final String ROOT = "com.yamilog.categoryservice";

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
