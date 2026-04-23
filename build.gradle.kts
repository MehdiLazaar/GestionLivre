import info.solidsoft.gradle.pitest.PitestPluginExtension
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.creating
import org.gradle.language.jvm.tasks.ProcessResources
import org.gradle.testing.jacoco.tasks.JacocoCoverageVerification
import org.gradle.testing.jacoco.tasks.JacocoReport

plugins {
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    id("org.springframework.boot") version "3.3.5"
    id("io.spring.dependency-management") version "1.1.7"
    jacoco
    id("info.solidsoft.pitest") version "1.15.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
}

group = "com.mehdiynov"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

sourceSets {
    create("testIntegration") {
        kotlin.srcDirs("src/testIntegration/kotlin")
        resources.setSrcDirs(listOf("src/testIntegration/resources"))
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }

    create("testComponent") {
        kotlin.srcDirs("src/testComponent/kotlin")
        resources.setSrcDirs(listOf("src/testComponent/resources"))
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }

    create("testArchitecture") {
        kotlin.srcDirs("src/testArchitecture/kotlin")
        resources.setSrcDirs(listOf("src/testArchitecture/resources"))
        compileClasspath += sourceSets["main"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }
}

configurations {
    named("testIntegrationImplementation") {
        extendsFrom(configurations["testImplementation"])
    }
    named("testIntegrationRuntimeOnly") {
        extendsFrom(configurations["testRuntimeOnly"])
    }

    named("testComponentImplementation") {
        extendsFrom(configurations["testImplementation"])
    }
    named("testComponentRuntimeOnly") {
        extendsFrom(configurations["testRuntimeOnly"])
    }

    named("testArchitectureImplementation") {
        extendsFrom(configurations["testImplementation"])
    }
    named("testArchitectureRuntimeOnly") {
        extendsFrom(configurations["testRuntimeOnly"])
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.liquibase:liquibase-core")
    implementation("org.postgresql:postgresql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springdoc:springdoc-openapi-starter-common:2.6.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.6.0")

    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest:kotest-property:5.9.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
    pitest("io.kotest.extensions:kotest-extensions-pitest:1.2.0")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }

    "testIntegrationImplementation"("io.mockk:mockk:1.13.8")
    "testIntegrationImplementation"("io.kotest:kotest-assertions-core:5.9.1")
    "testIntegrationImplementation"("io.kotest:kotest-runner-junit5:5.9.1")
    "testIntegrationImplementation"("io.kotest.extensions:kotest-extensions-spring:1.3.0")
    "testIntegrationImplementation"("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")
    "testIntegrationImplementation"("com.ninja-squad:springmockk:4.0.2")
    "testIntegrationImplementation"("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    "testIntegrationImplementation"("org.testcontainers:postgresql:1.19.1")
    "testIntegrationImplementation"("org.testcontainers:testcontainers:1.19.1")
    "testIntegrationImplementation"("org.testcontainers:jdbc-test:1.12.0")

    "testComponentImplementation"("org.junit.jupiter:junit-jupiter:5.10.2")
    "testComponentImplementation"("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    "testComponentImplementation"("io.cucumber:cucumber-java:7.14.0")
    "testComponentImplementation"("io.cucumber:cucumber-spring:7.14.0")
    "testComponentImplementation"("io.cucumber:cucumber-junit:7.14.0")
    "testComponentImplementation"("io.cucumber:cucumber-junit-platform-engine:7.14.0")
    "testComponentImplementation"("io.rest-assured:rest-assured:5.3.2")
    "testComponentImplementation"("org.junit.platform:junit-platform-suite:1.10.0")
    "testComponentImplementation"("org.testcontainers:postgresql:1.19.1")
    "testComponentImplementation"("org.testcontainers:testcontainers:1.19.1")
    "testComponentImplementation"("io.kotest:kotest-assertions-core:5.9.1")

    "testArchitectureImplementation"("com.tngtech.archunit:archunit-junit5:1.0.1")
    "testArchitectureImplementation"("io.kotest:kotest-assertions-core:5.9.1")
    "testArchitectureImplementation"("io.kotest:kotest-runner-junit5:5.9.1")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    reports {
        junitXml.required.set(true)
        html.required.set(true)
    }
}

val testIntegration by tasks.registering(Test::class) {
    description = "Runs integration tests."
    group = "verification"
    testClassesDirs = sourceSets["testIntegration"].output.classesDirs
    classpath = sourceSets["testIntegration"].runtimeClasspath
    shouldRunAfter(tasks.test)
    useJUnitPlatform()
}

val testComponent by tasks.registering(Test::class) {
    description = "Runs component tests."
    group = "verification"
    testClassesDirs = sourceSets["testComponent"].output.classesDirs
    classpath = sourceSets["testComponent"].runtimeClasspath
    shouldRunAfter(testIntegration)
    useJUnitPlatform()
}

val testArchitecture by tasks.registering(Test::class) {
    description = "Runs architecture tests."
    group = "verification"
    testClassesDirs = sourceSets["testArchitecture"].output.classesDirs
    classpath = sourceSets["testArchitecture"].runtimeClasspath
    shouldRunAfter(testComponent)
    useJUnitPlatform()
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.test, testIntegration, testComponent, testArchitecture)

    executionData.setFrom(
        fileTree(layout.buildDirectory.asFile.get()).include(
            "jacoco/test.exec",
            "jacoco/testIntegration.exec",
            "jacoco/testComponent.exec",
            "jacoco/testArchitecture.exec",
            "jacoco/*.exec"
        )
    )

    sourceDirectories.setFrom(files("src/main/kotlin"))
    classDirectories.setFrom(
        fileTree("build/classes/kotlin/main") {
            exclude(
                "**/*Application*",
                "**/*Config*",
                "**/*Configuration*",
                "**/*DTO*"
            )
        }
    )

    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
    }
}

tasks.named<JacocoCoverageVerification>("jacocoTestCoverageVerification") {
    dependsOn(tasks.named("jacocoTestReport"))

    executionData.setFrom(
        fileTree(layout.buildDirectory.asFile.get()).include(
            "jacoco/test.exec",
            "jacoco/testIntegration.exec",
            "jacoco/testComponent.exec",
            "jacoco/testArchitecture.exec",
            "jacoco/*.exec"
        )
    )

    sourceDirectories.setFrom(files("src/main/kotlin"))
    classDirectories.setFrom(
        fileTree("build/classes/kotlin/main") {
            exclude(
                "**/*Application*",
                "**/*Config*",
                "**/*Configuration*",
                "**/*DTO*"
            )
        }
    )

    violationRules {
        rule {
            limit {
                minimum = "0.50".toBigDecimal()
            }
        }
    }
}

pitest {
    junit5PluginVersion.set("1.2.1")
    targetClasses = setOf("com.mehdiynov.gestionLivre.domain.*")
    targetTests = setOf("com.mehdiynov.gestionLivre.domain.*")

    outputFormats = setOf("HTML", "XML")
    mutationThreshold = 0
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom(files("$rootDir/config/detekt.yml"))
}

tasks.named("check") {
    dependsOn(
        tasks.test,
        testIntegration,
        testComponent,
        testArchitecture,
        tasks.named("jacocoTestCoverageVerification"),
        tasks.named("detekt")
    )
}

tasks.named<ProcessResources>("processTestIntegrationResources") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named<ProcessResources>("processTestComponentResources") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named<ProcessResources>("processTestArchitectureResources") {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}