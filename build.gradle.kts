import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// ── Plugin Versions ───────────────────────────────────────────────────────────
plugins {
    id("org.springframework.boot")         version "3.3.5"  apply false
    id("io.spring.dependency-management")  version "1.1.6"  apply false
    id("org.jetbrains.kotlin.jvm")         version "1.9.25" apply false
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.25" apply false
}

// ── Version Catalog (allprojects 공유) ────────────────────────────────────────
val javaVersion         = JavaVersion.VERSION_21
val springBootVersion   = "3.3.5"
val springCloudVersion  = "2023.0.3"

// Library Versions
val mapstructVersion        = "1.5.5.Final"
val lombokVersion           = "1.18.34"
val testcontainersVersion   = "1.20.3"
val jwtVersion              = "0.12.6"
val kafkaVersion            = "3.8.0"
val resilience4jVersion     = "2.2.0"

// ── All Projects 공통 설정 ─────────────────────────────────────────────────────
allprojects {
    group   = "com.yamilog"
    version = "0.0.1-SNAPSHOT"
}

// ── Subprojects 공통 설정 ─────────────────────────────────────────────────────
subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    configure<JavaPluginExtension> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    // ── 공통 의존성 관리 (BOM) ──────────────────────────────────────────────────
    configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion")
            mavenBom("org.testcontainers:testcontainers-bom:$testcontainersVersion")
        }
        dependencies {
            dependency("org.mapstruct:mapstruct:$mapstructVersion")
            dependency("org.mapstruct:mapstruct-processor:$mapstructVersion")
            dependency("org.projectlombok:lombok:$lombokVersion")
            dependency("io.jsonwebtoken:jjwt-api:$jwtVersion")
            dependency("io.jsonwebtoken:jjwt-impl:$jwtVersion")
            dependency("io.jsonwebtoken:jjwt-jackson:$jwtVersion")
            dependency("io.github.resilience4j:resilience4j-spring-boot3:$resilience4jVersion")
        }
    }

    // ── 모든 서브모듈 공통 의존성 ────────────────────────────────────────────────
    dependencies {
        // Lombok
        "compileOnly"("org.projectlombok:lombok")
        "annotationProcessor"("org.projectlombok:lombok")
        "testCompileOnly"("org.projectlombok:lombok")
        "testAnnotationProcessor"("org.projectlombok:lombok")

        // Test
        "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        "testImplementation"("org.assertj:assertj-core")
        "testImplementation"("org.mockito:mockito-core")
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs.addAll(listOf("-parameters", "-Amapstruct.suppressGeneratorTimestamp=true"))
        options.encoding = "UTF-8"
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        jvmArgs("-XX:+EnableDynamicAgentLoading", "--add-opens", "java.base/java.lang=ALL-UNNAMED")
    }
}
