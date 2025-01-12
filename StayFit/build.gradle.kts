plugins {
    application
    kotlin("jvm") version "2.0.21"
}

group = "tg.stayfit"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:2.0.0") // Update to the latest version
    implementation("io.ktor:ktor-server-netty:2.0.0") // Update to the latest version
    implementation("io.ktor:ktor-gson:2.0.0") // Update to the latest version
    implementation("org.jetbrains.exposed:exposed-core:0.36.2")
    implementation("org.jetbrains.exposed:exposed-dao:0.36.2")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.36.2")
    implementation("com.h2database:h2:1.4.200")
    implementation("ch.qos.logback:logback-classic:1.2.6")

    // Optional: Add testing libraries
    testImplementation("io.ktor:ktor-server-tests:2.0.0") // Update to the latest version
    testImplementation("io.mockk:mockk:1.12.0") // For mocking
    testImplementation("io.kotest:kotest-runner-junit5:5.0.0") // For testing
}

application {
    mainClass.set("tg.stayfit.ApplicationKt")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11)) // Set the Java version
    }
}