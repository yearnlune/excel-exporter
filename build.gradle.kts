import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20"

    id("org.jetbrains.dokka") version "1.6.20"
    jacoco
    `maven-publish`
    signing
}

allprojects {
    group = "io.github.yearnlune.excel"
    version = "1.0.3"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}

dependencies {
    api("org.apache.poi:poi-ooxml:5.2.2")
    implementation("io.github.classgraph:classgraph:4.8.147")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.0")
    implementation("org.springframework:spring-web:5.3.20")
    implementation("com.amazonaws:aws-java-sdk-s3:1.12.241")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.8.2")
    testImplementation("io.findify:s3mock_2.13:0.2.6")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.0")
}

val dokkaJavadocJar by tasks.register<Jar>("dokkaJavadocJar") {
    dependsOn(tasks.dokkaJavadoc)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

tasks {
    test {
        useJUnitPlatform()

        finalizedBy(jacocoTestReport)
    }
    java {
        withSourcesJar()
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(dokkaJavadocJar)

            pom {
                name.set(rootProject.name)
                description.set("Export to excel")
                url.set("https://github.com/yearnlune/excel-exporter")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("yearnlune")
                        name.set("DONGHWAN KIM")
                        email.set("kdhpopyoa@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/yearnlune/excel-exporter.git")
                    developerConnection.set("scm:git:ssh://git@github.com:yearnlune/excel-exporter.git")
                    url.set("https://github.com/yearnlune/excel-exporter")
                }
            }
        }

    }
    repositories {
        maven {
            val releasesRepoUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsRepoUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl
            configure<SigningExtension> {
                val signingKey: String? by project
                val signingPassword: String? by project
                useInMemoryPgpKeys(signingKey, signingPassword)
                sign(publications["mavenJava"])
            }
            credentials {
                username = System.getenv("SONATYPE_USERNAME")
                password = System.getenv("SONATYPE_PASSWORD")
            }
        }
    }
}