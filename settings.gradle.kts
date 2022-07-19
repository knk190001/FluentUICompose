pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    
}

rootProject.name = "FluentUICompose"
include("Styles")
include("UI")
