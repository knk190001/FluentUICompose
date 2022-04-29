plugins {
    kotlin("jvm")
    java
}

group = "me.sfxde"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

buildscript {
    repositories {
        mavenCentral()
    }

    dependencies {
        classpath("com.github.knk190001.gradle-code-generator:plugin-kotlin") {
            version {
                branch = "master"
            }
        }
    }
}

apply(plugin="com.github.knk190001.gradle-code-generator-kotlin")