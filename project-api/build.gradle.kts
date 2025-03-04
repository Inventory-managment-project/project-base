plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor.plugin)
    alias(libs.plugins.kotlin.serialization.plugin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.dokka)
}

group = "mx.unam.fciencias.ids.eq1"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core.jvm)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.auth)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.html.dsl)
    implementation(libs.css.dsl)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.time)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.content.negotiation)
    implementation(libs.postgresql)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.client.plugin.contentneg)
    implementation(libs.ktor.auth.jwt)
    implementation(libs.apache.commons.codec)
    implementation(libs.ktor.call.logging)
    implementation(libs.ktor.cors)

    // DI
    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)
    implementation(libs.koin.annotations)
    ksp(libs.ksp.compiler)


    // Test Implementation
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.koin.test.junit5)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.h2)
}


tasks.test {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
    }

    // Set system properties for tests
    // systemProperty("some.property", "value")

    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).takeIf { it > 0 } ?: 1
}

ktor.docker.jreVersion = JavaVersion.VERSION_23
