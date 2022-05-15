import io.heartpattern.gcg.plugin.common.GenerateCodeTask
import io.heartpattern.gcg.plugin.kotlin.GradleCodeGeneratorKotlinPlugin
import org.jetbrains.kotlin.gradle.model.SourceSet.SourceSetType

plugins {
    kotlin("jvm")
    java
}

group = "me.sfxde"
version = "1.0"

apply(plugin = "com.github.knk190001.gradle-code-generator-kotlin")

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.compose.ui:ui:1.0.0")
}
val generatingSourceSet = sourceSets["mainGenerator"]!!
val generatingConfig = configurations[generatingSourceSet.implementationConfigurationName]!!
dependencies.add(generatingConfig.name,"org.json:json:20220320")
dependencies.add(generatingConfig.name,"org.jetbrains.compose.ui:ui:1.0.0")

buildscript {
    repositories {
        mavenCentral()
        maven {
            url = uri("https://jitpack.io")
        }
    }

    dependencies {
        classpath("com.github.knk190001:GradleCodeGenerator:1.0.5")
    }
}




