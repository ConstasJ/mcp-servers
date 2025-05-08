plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlin.jvm.plugin)
    implementation(libs.kotlinx.serialization.plugin)
    implementation(libs.ksp.plugin)
    implementation(libs.ktor.plugin)
}