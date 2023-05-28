import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20"
    application
    jacoco
}

group = "dev.gds"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation("io.mockk:mockk:1.13.4")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

kotlin {
    jvmToolchain(8)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.set(setOf("-Xjsr305=strict", "-Xcontext-receivers"))
        jvmTarget.set(JvmTarget.JVM_1_8)
        languageVersion.set(KotlinVersion.KOTLIN_2_0)
    }
}

tasks.jacocoTestReport {
    dependsOn("test")

    executionData.setFrom(fileTree(buildDir.resolve("jacoco")).apply {
        include("*.exec", "*.ec")
    })
    classDirectories.setFrom(classDirectories.files.map {
        fileTree(it).matching {
            exclude()
        }
    })

    sourceDirectories.setFrom(files("src/main/kotlin"))

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHml"))
    }
}

application {
    mainClass.set("MainKt")
}