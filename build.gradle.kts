plugins {
    `java-library`
    kotlin("jvm") version "2.1.20"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("com.github.ben-manes.versions") version "0.52.0"
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

    implementation("com.github.mizosoft.methanol:methanol:1.8.2")
    implementation("io.mikael:urlbuilder:2.0.9")

    testImplementation(enforcedPlatform("org.slf4j:slf4j-parent:2.0.17"))
    testImplementation(enforcedPlatform("ch.qos.logback:logback-parent:1.5.18"))
    testImplementation(enforcedPlatform("org.eclipse.jetty:jetty-project:11.0.25"))

    testImplementation("ch.qos.logback:logback-classic")
    testImplementation("org.slf4j:jul-to-slf4j:2.0.17")
    testImplementation("org.junit.jupiter:junit-jupiter:5.12.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("io.javalin:javalin:6.6.0")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.19.0")

    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.17.0")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
