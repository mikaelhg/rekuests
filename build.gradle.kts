import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_20
import org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.IGNORE
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    `java-library`
    kotlin("jvm") version "1.9.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.mikael"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    // implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.5.1")
    implementation("com.github.mizosoft.methanol:methanol:1.7.0")
    implementation("io.mikael:urlbuilder:2.0.9")

    testImplementation(enforcedPlatform("org.slf4j:slf4j-parent:2.0.9"))
    testImplementation(enforcedPlatform("ch.qos.logback:logback-parent:1.4.11"))
    testImplementation(enforcedPlatform("org.eclipse.jetty:jetty-project:11.0.15"))

    testImplementation("ch.qos.logback:logback-classic")
    testImplementation("org.slf4j:jul-to-slf4j:2.0.9")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("io.javalin:javalin:5.6.1")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.4.0")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
