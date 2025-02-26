plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor.plugin)
    alias(libs.plugins.kotlin.serialization.plugin)
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
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.content.negotiation)

    implementation(libs.koin.ktor)
    implementation(libs.koin.logger.slf4j)

    // Test Implementation
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.koin.test)
    testImplementation(libs.koin.test.junit4)
}
