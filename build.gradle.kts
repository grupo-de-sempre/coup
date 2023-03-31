plugins {
    kotlin("jvm") version "1.8.0"
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
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

kotlin {
    jvmToolchain(8)
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