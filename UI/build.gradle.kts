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
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(project(":Styles"))
    implementation("com.github.jnr:jnr-ffi:2.2.12")
}
