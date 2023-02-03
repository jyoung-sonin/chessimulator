pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

gradle.beforeProject {
    loadKeystoreProperties(project)
}

fun loadKeystoreProperties(project: Project) {
    java.util.Properties().apply {
        load(File(".sonin/key.properties").inputStream())
    }.forEach { (k, v) -> if (k is String) project.extra.set(k, v) }
}

rootProject.name = "Chessimulator"
include (":app")