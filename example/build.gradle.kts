plugins {
    kotlin("jvm")

    id("org.springframework.boot") version "2.7.0"
    kotlin("plugin.spring") version "1.6.21"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.7.0")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.239")
    implementation("io.findify:s3mock_2.13:0.2.6")
    implementation(rootProject)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}