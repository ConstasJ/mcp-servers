plugins {
    id("basic")
    alias(libs.plugins.ktor)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlinx.serialization)
}

dependencies {
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)
    implementation(libs.jsoup)
    ksp(libs.mcp.ksp)
    testImplementation(libs.kotlin.test)
    testImplementation(project(":test-utils"))
}