pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    
}
sourceControl {
    gitRepository(java.net.URI("https://github.com/knk190001/GradleCodeGenerator.git")){
        producesModule("com.github.knk190001.gradle-code-generator:plugin-kotlin")
    }
}

rootProject.name = "FluentUICompose"
include("Styles")
