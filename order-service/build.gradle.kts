plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
}

group = "mukul.food"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springframework.boot:spring-boot-starter-web")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

// disabled eureka discovery
//    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")

    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.springframework.kafka:spring-kafka:3.0.8")

    //jwt
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    //redis
    implementation ("org.springframework.boot:spring-boot-starter-data-redis")

    //avro for external event sharing
    implementation(project(":External-Contracts"))
    implementation("io.confluent:kafka-avro-serializer:7.6.0")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-avro")

}

tasks.test {
    useJUnitPlatform()
}