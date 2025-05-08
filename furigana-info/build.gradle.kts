plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

dependencies {
    implementation(libs.bundles.ktor)
}

kotlin {
    jvmToolchain(21)
}