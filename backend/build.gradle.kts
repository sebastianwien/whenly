plugins {
  java
  id("org.springframework.boot") version "4.0.0"
  id("io.spring.dependency-management") version "1.1.7"
}

group = "net.whenly"
version = "0.1.0-SNAPSHOT"

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(21)
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("org.springframework.boot:spring-boot-starter-mail")
  implementation("org.springframework.boot:spring-boot-starter-actuator")

  // Spring Boot 4 modularised the autoconfig: flyway-core alone is no
  // longer enough; the starter brings in spring-boot-flyway autoconfig too.
  implementation("org.springframework.boot:spring-boot-starter-flyway")
  implementation("org.flywaydb:flyway-database-postgresql")

  // ICS calendar generation
  implementation("net.sf.biweekly:biweekly:0.6.8")

  // QR code generation
  implementation("com.google.zxing:core:3.5.3")
  implementation("com.google.zxing:javase:3.5.3")

  runtimeOnly("org.postgresql:postgresql")

  developmentOnly("org.springframework.boot:spring-boot-devtools")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.testcontainers:junit-jupiter:1.20.4")
  testImplementation("org.testcontainers:postgresql:1.20.4")
  testRuntimeOnly("com.h2database:h2")
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<JavaCompile> {
  options.compilerArgs.add("-parameters")
}
