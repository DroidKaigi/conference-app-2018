import org.gradle.api.JavaVersion.VERSION_1_7
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.version
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", Versions.kotlin))
    }
}

plugins {
    id("java-library")
    kotlin("jvm") version Versions.kotlin
}
apply {
    plugin("kotlin")
}

dependencies {
    compile(kotlin("stdlib-jre7", Versions.kotlin))
}

java {
    sourceCompatibility = VERSION_1_7
    targetCompatibility = VERSION_1_7
}
