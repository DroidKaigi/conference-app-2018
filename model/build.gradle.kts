import org.gradle.api.JavaVersion.VERSION_1_7
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.version
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version: String by extra
buildscript {
    var kotlin_version: String by extra
    kotlin_version = "1.2.20"
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin", kotlin_version))
    }
}

plugins {
    id("java-library")
    kotlin("jvm") version "1.2.20"
}
apply {
    plugin("kotlin")
}

dependencies {
    kotlin("stdlib-jre7", "1.2.20")
    compile(kotlinModule("stdlib-jdk8", kotlin_version))
}

java {
    sourceCompatibility = VERSION_1_7
    targetCompatibility = VERSION_1_7
}
repositories {
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
