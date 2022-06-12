import io.heartpattern.gcg.plugin.common.GenerateCodeTask
import io.heartpattern.gcg.plugin.kotlin.GradleCodeGeneratorKotlinPlugin
import org.jetbrains.kotlin.gradle.model.SourceSet.SourceSetType

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.5.31"
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

val generatingSourceSet = sourceSets["mainGenerator"]!!
val generatingConfig = configurations[generatingSourceSet.implementationConfigurationName]!!
dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.compose.ui:ui:1.0.0")
    implementation("org.json:json:20220320")
    implementation(sourceSets["mainGenerator"].output)

    generatingConfig("org.json:json:20220320")
    generatingConfig("org.jetbrains.compose.ui:ui:1.0.0")
    generatingConfig("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    generatingConfig("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
}

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




