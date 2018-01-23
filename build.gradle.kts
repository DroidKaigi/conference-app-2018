import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

buildscript {
    apply(mapOf("from" to "${rootDir.absolutePath}/versions.gradle"))

    repositories {
        google()
        jcenter()
        maven(url="https://plugins.gradle.org/m2/")
        maven(url="https://maven.fabric.io/public")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.20")
        classpath("com.google.gms:google-services:3.1.1")
        classpath("gradle.plugin.org.jlleitschuh.gradle:ktlint-gradle:3.0.0")
        classpath("io.fabric.tools:gradle:1.25.1")
        classpath("com.github.ben-manes:gradle-versions-plugin:0.17.0")
        classpath("com.google.gms:oss-licenses:0.9.1")
        classpath("com.deploygate:gradle:1.1.4")
        classpath("com.github.triplet.gradle:play-publisher:1.2.0")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}
