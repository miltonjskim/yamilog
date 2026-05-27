// ── Plugin Management (반드시 파일 최상단) ────────────────────────────────────
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

// ── Dependency Resolution ─────────────────────────────────────────────────────
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "yamilog"

// ── Common Modules ────────────────────────────────────────────────────────────
include(":common:common-domain")
include(":common:common-infra")

// ── Gateway ───────────────────────────────────────────────────────────────────
include(":gateway")

// ── Microservices ─────────────────────────────────────────────────────────────
include(":services:user-service")
include(":services:category-service")
include(":services:place-service")
include(":services:review-service")
include(":services:feed-service")
include(":services:level-engine")
include(":services:search-service")
include(":services:ad-service")
