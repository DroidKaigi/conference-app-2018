import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.maven
import org.gradle.kotlin.dsl.repositories

allprojects {
    val compileSdk       by extra {27}
    val buildTools       by extra {"27.0.2"}
    val minSdk           by extra {19}
    val targetSdk        by extra {27}
    val gradleBuildTool  by extra {"3.0.1"}
    val kotlin           by extra {"1.2.20"}
//    val googleServices   by extra {"3.1.1"}
//    val ktlint           by extra {"0.14.0"}
//    val ktlintGradle     by extra {"3.0.0"}
//    val fabricGradleTool by extra {"1.25.1"}
//    val gradleVersions   by extra {"0.17.0"}
    val supportLibrary   by extra {"27.0.2"}
    val retrofit         by extra {"2.3.0"}
    val kotshi           by extra {"0.3.0"}
    val arch             by extra {"1.0.0"}
    val dagger           by extra {"2.13"}
    val firebase         by extra {"11.6.2"}
    val kotpref          by extra {"2.3.0"}
    val glide            by extra {"4.4.0"}
    val groupie          by extra {"2.0.2"}
    val stetho           by extra {"1.5.0"}
    val debot            by extra {"2.0.3"}
//    val ossLicenses      by extra {"0.9.1"}
//    val deploygate       by extra {"1.1.4"}
//    val playPublisher    by extra {"1.2.0"}
    val robolectric      by extra {"3.5.1"}

    repositories {
        google()
        jcenter()
        maven(url="https://plugins.gradle.org/m2/")
        maven(url="https://maven.fabric.io/public")
    }
}

task("clean", Delete::class) {
    delete = setOf(rootProject.buildDir)
}
