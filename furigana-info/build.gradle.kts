plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
}

dependencies {
    implementation(libs.bundles.ktor)
}

application {
    mainClass.set("io.github.constasj.mcp.furigana.MainKt")
}

kotlin {
    jvmToolchain(21)
}