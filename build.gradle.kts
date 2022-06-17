import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"

    jacoco
}

allprojects {
    group = "io.github.yearnlune.excel"
    version = "1.0.0"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
    }
}

dependencies {
    implementation("org.apache.poi:poi-ooxml:5.2.2")
    implementation("io.github.classgraph:classgraph:4.8.147")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.0")
    implementation("org.springframework:spring-web:5.3.20")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.241")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks {
    test {
        useJUnitPlatform()

        finalizedBy(jacocoTestReport)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
