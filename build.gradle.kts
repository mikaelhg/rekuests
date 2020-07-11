plugins {
    kotlin("jvm") version "1.3.72"
    `java-library`
}

group = "io.mikael"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testImplementation(enforcedPlatform("org.slf4j:slf4j-parent:1.7.30"))
    testImplementation(enforcedPlatform("ch.qos.logback:logback-parent:1.2.3"))
    testImplementation("ch.qos.logback:logback-classic")
    testImplementation("org.slf4j:jul-to-slf4j:1.7.30")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation("org.mock-server:mockserver-junit-jupiter:5.11.0")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
