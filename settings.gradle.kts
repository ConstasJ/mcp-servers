rootProject.name = "mcp-servers-root"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

val projects = listOf(
    "furigana-info",
    "test-utils"
)

include(projects)
