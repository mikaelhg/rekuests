plugins {
    `java-library`
    kotlin("jvm") version "2.2.21"
    id("com.gradleup.shadow") version "9.2.2"
    id("com.github.ben-manes.versions") version "0.52.0"
}

group = "io.mikael"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("com.github.mizosoft.methanol:methanol:1.8.4")
    implementation("io.mikael:urlbuilder:2.0.9")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.20.0")

    testImplementation(enforcedPlatform("org.slf4j:slf4j-parent:2.0.17"))
    testImplementation(enforcedPlatform("ch.qos.logback:logback-parent:1.5.20"))
    testImplementation(enforcedPlatform("org.eclipse.jetty:jetty-project:11.0.26"))

    testImplementation("ch.qos.logback:logback-classic")
    testImplementation("org.slf4j:jul-to-slf4j:2.0.17")
    testImplementation(kotlin("test"))

    testImplementation("io.javalin:javalin:6.7.0")

    testImplementation("org.mockito.kotlin:mockito-kotlin:6.1.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.20.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
