import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.jetbrains.kotlin.konan.properties.Properties

/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin library project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.10.2/userguide/building_java_projects.html in the Gradle documentation.
 * This project uses @Incubating APIs which are subject to change.
 */
group = "tech.s-co"
version = "0.2.5"

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    // Apply the java-library plugin for API and implementation separation.
    `java-library`
//    `maven-publish`
//    signing

    alias(libs.plugins.detek)
    alias(libs.plugins.maven.deployer)
    alias(libs.plugins.git.version)
}

gitVersioning.apply {
    refs {
        tag("(?<version>.*)") {
            version = "\${ref.version}"
        }
    }
}

// Load local.properties
val localProperties = Properties()
file(rootDir.path + "/local.properties").takeIf { it.exists() }?.apply {
    inputStream().use { localProperties.load(it) }
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
    testImplementation(libs.kotest.assertions.json)
    testImplementation(libs.konsist)
    testImplementation(libs.ktor.client.mock)
}

publishing {
    publications {
        create<MavenPublication>("mavenKotlin") {
            artifactId = rootProject.name
            groupId = "tech.s-co"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name = rootProject.name
                description = """
                    Hetzner Cloud API Kotlin library
                    https://docs.hetzner.cloud
                """.trimIndent()
                url = "https://github.com/sasa-b/hetznerkloud"
                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }
                developers {
                    developer {
                        id = "sasa-b"
                        name = "Sasha Blagojevic"
                        email = "sasa.blagojevic@mail.com"
                    }
                }

                scm {
                    url = "https://github.com/sasa-b/hetznerkloud"
                    connection = "scm:git:git://github.com/sasa-b/hetznerkloud"
                    developerConnection = "scm:git:ssh://github.com/sasa-b/hetznerkloud"
                }
            }
        }
    }

    repositories {
        maven {
            name = "file"
            url = uri(layout.buildDirectory.dir("repo"))
        }

//        maven {
//            name = "sasa-bHetznerkloud"
// //            url = uri("https://maven.pkg.github.com/sasa-b/hetznerkloud")
//            url = uri("https://maven.pkg.github.com/sasa-b/hetznerkloud")
//            flatDir {
//                dirs(layout.buildDirectory.dir("repo/tech/s-co/hetznerkloud/$version"))
//            }
//
//            credentials {
//                username = localProperties.getProperty("GITHUB_USER")!!
//                password = localProperties.getProperty("GITHUB_TOKEN")!!
//            }
//        }

//        maven {
//            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
//            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
//            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
//
//            credentials {
//                username = project.findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME")
//                password = project.findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD")
//            }
//        }
    }
}

signing {
    sign(publishing.publications["mavenKotlin"])
}

deployer {
    // Load properties from gradle.properties first (already done by default)

    // 1. Artifact definition.
    // https://opensource.deepmedia.io/deployer/artifacts
    content {
        component {
            fromMavenPublication("mavenKotlin")
        }
    }

    // 2. Project details.
    // https://opensource.deepmedia.io/deployer/configuration
    projectInfo {
        description = """
            Hetzner Cloud API Kotlin library
            https://docs.hetzner.cloud
        """.trimIndent()
        url = "https://github.com/sasa-b/hetznerkloud"
        scm.fromGithub("sasa-b", "hetznerkloud")
        license(apache2)
        developer(
            name = "Sasha Blagojevic",
            email = "sasa.blagojevic@mail.com",
            url = "https://s-co.tech",
        )
        artifactId = rootProject.name
        groupId = group.toString()
    }

    // 3. Central Portal configuration.
    // https://opensource.deepmedia.io/deployer/repos/central-portal
    centralPortalSpec {
        signing.key = secret("SIGNING_KEY")
        signing.password = secret("SIGNING_PASSPHRASE")
        auth.user = secret("MAVEN_USERNAME")
        auth.password = secret("MAVEN_PASSWORD")

        allowMavenCentralSync = false
    }

    githubSpec {
        owner.set("sasa-b")
        repository.set("hetznerkloud")

        // Personal GitHub username and a personal access token linked to it
        auth.user.set(secret("GITHUB_USER"))
        auth.token.set(secret("GITHUB_TOKEN"))
    }

    release {
        release.description.set("Deployer release v$version")
    }
}

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
    jvmTarget = "1.8"
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "1.8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    withSourcesJar()
    withJavadocJar()

    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// To prevent ./gradlew publish from failing on Github tasks
// we can alos use project.afterEvaluate
// gradle.projectsEvaluated {
//    tasks.withType<PublishToMavenRepository>().configureEach {
//        if (name.endsWith("publishMavenKotlinPublicationToSasa-bHetznerkloudRepository")) {
//            repository.credentials {
//                if (username == null) {
//                    username = localProperties.getProperty("GITHUB_USER")!!
//                }
//                if (password == null) {
//                    password = localProperties.getProperty("GITHUB_TOKEN")!!
//                }
//            }
//        }
//    }
// }
