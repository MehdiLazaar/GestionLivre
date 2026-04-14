import info.solidsoft.gradle.pitest.PitestPluginExtension

plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.13"
	id("io.spring.dependency-management") version "1.1.7"
    jacoco
    id("info.solidsoft.pitest") version "1.15.0"
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

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("io.kotest:kotest-property:5.9.1")
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.kotest.extensions:kotest-extensions-pitest:1.2.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("info.solidsoft.gradle.pitest:gradle-pitest-plugin:1.15.0")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

jacoco {
    toolVersion = "0.8.14"
    reportsDirectory = layout.buildDirectory.dir("customJacocoReportDir")
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report

    reports {
        xml.required = false
        csv.required = false
        html.required = true
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.5".toBigDecimal()
            }
        }

        rule {
            isEnabled = false
            element = "CLASS"
            includes = listOf("org.gradle.*")

            limit {
                counter = "LINE"
                value = "TOTALCOUNT"
                maximum = "0.3".toBigDecimal()
            }
        }
    }
}

configure<PitestPluginExtension> {
    targetClasses.set(listOf("com.mehdiynov.gestionLivre.*"))
}

pitest {
    targetClasses.addAll(listOf("com.mehdiynov.gestionLivre.*"))

    junit5PluginVersion = "1.1.2"
    threads.set(2)
    outputFormats.set(listOf("HTML"))

    timestampedReports.set(false)

    excludedClasses.set(listOf("*.Companion*", "*.Config*"))
    mutators.set(listOf("STRONGER"))
    mainSourceSets.addAll(sourceSets.main.get())
    testSourceSets.addAll(sourceSets.test.get())
}