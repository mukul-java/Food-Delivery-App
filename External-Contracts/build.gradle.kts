plugins {
    id("java-library")
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1"
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}

group = "mukul.contracts"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()

    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
}

dependencies {
    compileOnly ("org.projectlombok:lombok:1.18.30")
    annotationProcessor ("org.projectlombok:lombok:1.18.30")
    implementation("org.apache.avro:avro:1.11.3")

}
