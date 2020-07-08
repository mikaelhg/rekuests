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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
}

tasks {
    test {
        useJUnitPlatform()
    }
}
