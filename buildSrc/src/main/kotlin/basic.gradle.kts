plugins {
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(libs.bundles.mcp)
}

kotlin {
    jvmToolchain(21)
}