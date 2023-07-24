plugins {
    `java-library`
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "io.mikael"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    // implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.5.1")
    implementation("com.github.mizosoft.methanol:methanol:1.7.0")

    testImplementation(enforcedPlatform("org.slf4j:slf4j-parent:2.0.7"))
    testImplementation(enforcedPlatform("ch.qos.logback:logback-parent:1.4.8"))
    testImplementation(enforcedPlatform("org.eclipse.jetty:jetty-project:11.0.15"))

    testImplementation("ch.qos.logback:logback-classic")
    testImplementation("org.slf4j:jul-to-slf4j:2.0.7")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
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
