plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.ms.report"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Framework
	implementation("org.springframework.boot:spring-boot-starter")

	// Kotlin
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

	// AWS SDK
	implementation("software.amazon.awssdk:sqs:2.21.0")
	implementation("software.amazon.awssdk:s3:2.21.0")
	implementation("software.amazon.awssdk:core:2.21.0")

	// Geração de relatórios
	implementation("org.apache.poi:poi:5.2.3")             // Excel
	implementation("org.apache.poi:poi-ooxml:5.2.3")       // Excel XLSX
	implementation("com.itextpdf:itext7-core:7.2.5")       // PDF
	implementation("org.xhtmlrenderer:flying-saucer-pdf:9.1.22") // HTML para PDF

	// Templates HTML (para emails e relatórios)
	implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")

	// Logging estruturado
	implementation("net.logstash.logback:logstash-logback-encoder:7.4")

	// HTTP Client (para integrações)
	implementation("org.springframework.boot:spring-boot-starter-webflux")

	// Testes
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.mockk:mockk:1.13.5")
}
kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
