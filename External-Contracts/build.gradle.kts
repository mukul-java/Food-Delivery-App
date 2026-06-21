import org.gradle.kotlin.dsl.compileOnly

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
}

group = "mukul.contracts"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly ("org.projectlombok:lombok:1.18.30")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")
    implementation("org.apache.avro:avro:1.11.3")

}
