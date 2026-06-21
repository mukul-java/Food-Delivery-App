plugins {
    id("org.springframework.boot") version "3.2.5" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    id("java")
}

group = "mukul.food"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://packages.confluent.io/maven/")
        }
    }
}

subprojects {

    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }

    dependencies {
        implementation(platform("org.springframework.boot:spring-boot-dependencies:3.2.5"))
        implementation(platform("org.springframework.cloud:spring-cloud-dependencies:2023.0.1"))

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

    }

}