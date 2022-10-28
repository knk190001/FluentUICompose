import org.jetbrains.compose.compose

plugins {
    id("java")
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "me.sfxde"
version = "1.0"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(project(":Styles"))
    implementation("com.github.jnr:jnr-ffi:2.2.12")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.31")
    implementation("com.github.knk190001:EasyHookJava:1.0.6")
    implementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

