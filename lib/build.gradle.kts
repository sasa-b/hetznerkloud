import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.10.2/userguide/building_java_projects.html in the Gradle documentation.
 * This project uses @Incubating APIs which are subject to change.
 */
group = "tech.s-co"
version = "0.1.0"

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)

    // Apply the java-library plugin for API and implementation separation.
//    `maven-publish`
//    signing

    alias(libs.plugins.detek)
    alias(libs.plugins.maven.deployer)
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // This dependency is exported to consumers, that is to say found on their compile classpath.
//    api(libs.commons.math3)

    // This dependency is used internally, and not exposed to consumers on their own compile classpath.
//    implementation(libs.guava)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.serialization)
    implementation(libs.ktor.client.content.negotiation)

    testImplementation(libs.kotest.runner.junit5)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.konsist)
    testImplementation(libs.ktor.client.mock)
}

// testing {
//    suites {
//        // Configure the built-in test suite
//        val test by getting(JvmTestSuite::class) {
//            // Use Kotlin Test test framework
//            useKotlinTest("2.0.0")
//        }
//    }
// }

detekt {
    buildUponDefaultConfig = true // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config.setFrom("$projectDir/src/main/resources/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
//    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true) // observe findings in your browser with structure and code snippets
        xml.required.set(false) // checkstyle like format mainly for integrations like Jenkins
        sarif.required.set(false) // standardized SARIF format (https://sarifweb.azurewebsites.net/) to support integrations with GitHub Code Scanning
        md.required.set(true) // simple Markdown format
    }
}

// Kotlin DSL
tasks.withType<Detekt>().configureEach {
    jvmTarget = "21"
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "21"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}
