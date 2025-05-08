plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.ksp)
}

dependencies {
    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)
    implementation(libs.bundles.mcp)
    implementation(libs.jsoup)
    ksp(libs.mcp.ksp)
}

application {
    mainClass.set("io.github.constasj.mcp.furigana.MainKt")
}

kotlin {
    jvmToolchain(21)
}