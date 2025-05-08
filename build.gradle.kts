allprojects {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/Stream29/mcp-ksp")
            credentials {
                username = project.findProperty("github.user") as String? ?: System.getenv("GITHUB_USER")
                password = project.findProperty("github.pat") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, TimeUnit.MINUTES)
    }
}