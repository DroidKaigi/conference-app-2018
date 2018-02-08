import com.android.build.gradle.api.ApplicationVariant
import de.triplet.gradle.play.PlayAccountConfig
import groovy.lang.Closure
import org.apache.tools.ant.types.optional.depend.DependScanner
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.version
import org.gradle.kotlin.dsl.*
import org.jlleitschuh.gradle.ktlint.ReporterType

plugins {
    id("com.android.application") version Versions.gradleBuildTool
    kotlin("android") version Versions.kotlin
    kotlin("kapt") version Versions.kotlin
    id("org.jlleitschuh.gradle.ktlint") version Versions.ktlintGradle
    id("io.fabric") version Versions.fabricGradleTool
    id("com.google.gms.oss.licenses.plugin") version Versions.ossLicenses
    id("com.github.ben-manes.versions") version Versions.gradleVersions
    id("deploygate") version Versions.deploygate
    id("com.github.triplet.play") version Versions.playPublisher
    id("com.google.gms.google-services") version Versions.googleServices apply false
}

// Manifest version
val versionMajor = 1
val versionMinor = 0
val versionPatch = 0

android {
    compileSdkVersion(Versions.compileSdk)
    buildToolsVersion(Versions.buildTools)
    dataBinding.isEnabled = true

    defaultConfig {
        applicationId = "io.github.droidkaigi.confsched2018"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = versionMajor * 10000 + versionMinor * 100 + versionPatch
        versionName = "$versionMajor.$versionMinor.$versionPatch"
        testInstrumentationRunner = "io.github.droidkaigi.confsched2018.test.TestAppRunner"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        resConfigs("en", "ja")

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }
    applicationVariants.all(object : Action<ApplicationVariant> {
        override fun execute(variant: ApplicationVariant) {
            variant.resValue("string", "versionInfo", variant.versionName)
        }

    })
    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create("release") {
            storeFile = rootProject.file("release.keystore")
            storePassword = System.getenv("ANDROID_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("ANDROID_KEYSTORE_ALIAS")
            keyPassword = System.getenv("ANDROID_KEYSTORE_PRIVATE_KEY_PASSWORD")
        }
    }
    buildTypes {
        getByName("debug") {
            manifestPlaceholders = mapOf("scheme" to "conference", "host" to "droidkaigi.co.jp.debug")
            resValue("string", "app_name", "DroidKaigi 2018 Dev")
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        getByName("release") {
            manifestPlaceholders = mapOf("scheme" to "conference", "host" to "droidkaigi.co.jp")
            resValue("string", "app_name", "DroidKaigi 2018")
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            isZipAlignEnabled = true
            isMinifyEnabled = true
            proguardFile(getDefaultProguardFile("proguard-android.txt"))
            // global proguard settings
            proguardFile(file("proguard-rules.pro"))
            // library proguard settings
            val files = rootProject.file("proguard")
                    .listFiles()
                    .filter { it.name.startsWith("proguard") }
                    .toTypedArray()
            proguardFiles(*files)
        }
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
        unitTests.isIncludeAndroidResources = true
    }
    lintOptions {
        lintConfig = file("lint.xml")
        textReport = true
        textOutput("stdout")
    }

}

kapt {
    useBuildCache = true
}

dependencies {

    implementation(project(":model"))

    // Kotlin
    implementation(Depends.Kotlin.stdlib)
    implementation(Depends.ktx)

//    //==================== Support Library ====================
    implementation(Depends.Support.support_v4)
    implementation(Depends.Support.appcompat_v7)
    implementation(Depends.Support.design)
    implementation(Depends.Support.cardview_v7)
    implementation(Depends.Support.customtabs)
    implementation(Depends.Support.constraint)
    implementation(Depends.Support.multidex)
    implementation(Depends.Support.support_emoji)
    implementation(Depends.Support.preference_v7)
    implementation(Depends.Support.preference_v14)

//==================== Network ====================
    implementation(Depends.OkHttp3.loggingIntercepter)

    implementation(Depends.Retrofit.core)
    implementation(Depends.Retrofit.converterMoshi)
    implementation(Depends.Retrofit.adapterRxJava2)

//==================== Structure ====================
    implementation(Depends.Kotshi.api)
    kapt(Depends.Kotshi.compiler)

    implementation(Depends.LifeCycle.runtime)
    implementation(Depends.LifeCycle.extensions)
    implementation(Depends.LifeCycle.reactivestreams)
    implementation(Depends.Room.runtime)
    implementation(Depends.Room.rxjava2)
    kapt(Depends.Room.compiler)

    implementation(Depends.RxJava2.core)
    implementation(Depends.RxJava2.android)
    implementation(Depends.RxJava2.kotlin)
    implementation(Depends.rxbroadcast)

    kapt(Depends.Binding.compiler)

    implementation(Depends.Dagger.core)
    implementation(Depends.Dagger.android)
    implementation(Depends.Dagger.androidSupport)
    kapt(Depends.Dagger.compiler)
    kapt(Depends.Dagger.androidProcessor)

    implementation(Depends.PlayService.map)

    implementation(Depends.Firebase.firestore)
    implementation(Depends.Firebase.auth)
    implementation(Depends.Firebase.core)
    implementation(Depends.Firebase.messaging)

    implementation(Depends.threetenabp)

    implementation(Depends.Kotpref.kotpref)
    implementation(Depends.Kotpref.initializer)
    implementation(Depends.Kotpref.enumSupport)

//==================== UI ====================
    implementation(Depends.Glide.core)
    implementation(Depends.Glide.okhttp3)
    kapt(Depends.Glide.compiler)

    implementation(Depends.Groupie.core)
    implementation(Depends.Groupie.binding)

    implementation(Depends.downloadableCalligraphy)
    implementation(Depends.PlayService.oss)

//==================== Debug ====================
    debugImplementation(Depends.Stetho.core)
    debugImplementation(Depends.Stetho.okhttp3)

    implementation(Depends.crashlytics) {
        isTransitive = true
    }

    implementation(Depends.timber)

    debugImplementation(Depends.leakcanary)

    debugImplementation(Depends.Debot.core)
    releaseImplementation(Depends.Debot.noop)

//==================== Test ====================
    testImplementation(Depends.junit)
    testImplementation(Depends.mockitoKotlin)

    testImplementation(Depends.Robolectric.core)
    testImplementation(Depends.Robolectric.multidex)
    testImplementation(Depends.assertk)

    testImplementation(Depends.threetenbp)

    androidTestImplementation(Depends.SupportTest.runner)
    androidTestImplementation(Depends.SupportTest.espresso)
    androidTestImplementation(Depends.SupportTest.contrib)
    androidTestImplementation(Depends.assertk)
}

repositories {
    mavenCentral()
}

play {
    uploadImages = true
    setTrack("alpha") // 'production' or 'rollout' or 'beta' or 'alpha'
    untrackOld = true
    // userFraction = 0.1
    jsonFile = file("publisher-keys.json")
}

ktlint {
    version = Versions.ktlint
    android = true
    reporter = ReporterType.CHECKSTYLE
    ignoreFailures = true
}

apply(mapOf("plugin" to "com.google.gms.google-services"))

deploygate {
    userName = "takahirom"
    token = System.getenv("DEPLOY_GATE_API_KEY")

    apks {
        create("release") {
            val hash = Runtime.getRuntime().exec("git rev-parse --short HEAD").inputStream.reader().use { it.readText() }.trim()
            message = "https://github.com/DroidKaigi/conference-app-2018/tree/$hash ${System.getenv("CIRCLE_BUILD_URL")}"

            distributionKey = "aed2445665e27de6571227992d66ea489b6bdb44"
            releaseNote = "https://github.com/DroidKaigi/conference-app-2018/tree/$hash ${System.getenv("CIRCLE_BUILD_URL")}"
        }
    }
}
