import com.android.build.gradle.api.ApplicationVariant
import de.triplet.gradle.play.PlayAccountConfig
import groovy.lang.Closure
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.repositories
import org.gradle.kotlin.dsl.version
import org.gradle.kotlin.dsl.*
import org.jlleitschuh.gradle.ktlint.ReporterType

// Manifest version
val versionMajor = 0
val versionMinor = 1
val versionPatch = 0
val compileSdk: Int by extra
val buildTools: String by extra
val minSdk: Int by extra
val targetSdk: Int by extra
val gradleBuildTool: String by extra
val kotlin: String by extra
//val googleServices   :String by extra
//val ktlint           :String by extra
//val ktlintGradle     :String by extra
//val fabricGradleTool :String by extra
//val gradleVersions   :String by extra
val supportLibrary: String by extra
val retrofit: String by extra
val kotshi: String by extra
val arch: String by extra
val dagger: String by extra
val firebase: String by extra
val kotpref: String by extra
val glide: String by extra
val groupie: String by extra
val stetho: String by extra
val debot: String by extra
//val ossLicenses      :String by extra
//val deploygate       :String by extra
//val playPublisher    :String by extra
val robolectric: String by extra

plugins {
    id("com.android.application") version "3.0.1"
    kotlin("android") version "1.2.20"
    kotlin("kapt") version "1.2.20"
    id("org.jlleitschuh.gradle.ktlint") version "3.0.0"
    id("io.fabric") version "1.25.1"
    id("com.google.gms.oss.licenses.plugin") version "0.9.1"
    id("com.github.ben-manes.versions") version "0.17.0"
    id("deploygate") version "1.1.4"
    id("com.github.triplet.play") version "1.2.0"
    id("com.google.gms.google-services") version "3.1.1" apply false
}

android {
    compileSdkVersion(compileSdk)
    buildToolsVersion(buildTools)
    dataBinding {
        isEnabled = true
    }

    // Play publisher
    //TODO playAccountConfigs を直接かけないので追加する必要がある
    container(PlayAccountConfig::class.java).create("playAccountConfigs") {
        jsonFile = file("publisher-keys.json")
    }
//    playAccountConfigs.defaultAccountConfig {
//        // TODO Replace json file to the one which is generated in the API console.
//        // https://github.com/Triple-T/gradle-play-publisher#authentication
//        jsonFile = file("publisher-keys.json")
//    }

    defaultConfig {
        applicationId = "io.github.droidkaigi.confsched2018"
        minSdkVersion(minSdk)
        targetSdkVersion(targetSdk)
        versionCode = versionMajor * 10000 + versionMinor * 100 + versionPatch
        versionName = "$versionMajor.$versionMinor.$versionPatch"
        testInstrumentationRunner = "io.github.droidkaigi.confsched2018.test.TestAppRunner"
        vectorDrawables.useSupportLibrary = true
        multiDexEnabled = true
        resConfigs("en", "ja")
//        playAccountConfig = playAccountConfigs.defaultAccountConfig

        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }
    applicationVariants.all(object:Action<ApplicationVariant>{
        override fun execute(variant:ApplicationVariant){
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
            setProguardFiles(files)
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
    implementation(kotlin("stdlib-jre7", kotlin))

//    //==================== Support Library ====================
    implementation("com.android.support:support-v4:$supportLibrary")
    implementation("com.android.support:appcompat-v7:$supportLibrary")
    implementation("com.android.support:design:$supportLibrary")
    implementation("com.android.support:cardview-v7:$supportLibrary")
    implementation("com.android.support:customtabs:$supportLibrary")
    implementation("com.android.support.constraint:constraint-layout:1.1.0-beta4")
    implementation("com.android.support:multidex:1.0.2")
    implementation("com.android.support:support-emoji-appcompat:$supportLibrary")
    implementation("com.android.support:preference-v7:$supportLibrary")
    implementation("com.android.support:preference-v14:$supportLibrary")

//==================== Network ====================
    implementation("com.squareup.okhttp3:logging-interceptor:3.9.1")

    implementation("com.squareup.retrofit2:retrofit:$retrofit")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit")
    implementation("com.squareup.retrofit2:adapter-rxjava2:$retrofit")

//==================== Structure ====================
    implementation("se.ansman.kotshi:api:$kotshi")
    kapt("se.ansman.kotshi:compiler:$kotshi")

    implementation("android.arch.lifecycle:runtime:$arch")
    implementation("android.arch.lifecycle:extensions:$arch")
    implementation("android.arch.lifecycle:reactivestreams:$arch")
    implementation("android.arch.persistence.room:runtime:$arch")
    implementation("android.arch.persistence.room:rxjava2:$arch")
    kapt("android.arch.persistence.room:compiler:$arch")

    implementation("io.reactivex.rxjava2:rxjava:2.1.8")
    implementation("io.reactivex.rxjava2:rxandroid:2.0.1")
    implementation("io.reactivex.rxjava2:rxkotlin:2.2.0")

    kapt("com.android.databinding:compiler:3.0.1")

    implementation("com.google.dagger:dagger:$dagger")
    implementation("com.google.dagger:dagger-android:$dagger")
    implementation("com.google.dagger:dagger-android-support:$dagger")
    kapt("com.google.dagger:dagger-compiler:$dagger")
    kapt("com.google.dagger:dagger-android-processor:$dagger")

    implementation("com.google.android.gms:play-services-maps:$firebase")

    implementation("com.google.firebase:firebase-firestore:$firebase")
    implementation("com.google.firebase:firebase-auth:$firebase")
    implementation("com.google.firebase:firebase-core:$firebase")

    implementation("com.jakewharton.threetenabp:threetenabp:1.0.5")

    implementation("com.chibatching.kotpref:kotpref:$kotpref")
    implementation("com.chibatching.kotpref:initializer:$kotpref")

//==================== UI ====================
    implementation("com.github.bumptech.glide:glide:$glide")
    implementation("com.github.bumptech.glide:okhttp3-integration:$glide")
    kapt("com.github.bumptech.glide:compiler:$glide")

    implementation("com.xwray:groupie:$groupie")
    implementation("com.xwray:groupie-databinding:$groupie")

    implementation("com.github.takahirom.downloadable.calligraphy:downloadable-calligraphy:0.1.2")
    implementation("com.google.android.gms:play-services-oss-licenses:11.6.2")

//==================== Debug ====================
    debugImplementation("com.facebook.stetho:stetho:$stetho")
    debugImplementation("com.facebook.stetho:stetho-okhttp3:$stetho")

    implementation("com.crashlytics.sdk.android:crashlytics:2.7.1@aar") {
        isTransitive = true
    }

    implementation("com.jakewharton.timber:timber:4.6.0")

    debugImplementation("com.squareup.leakcanary:leakcanary-android:1.5.4")

    debugImplementation("com.tomoima.debot:debot:$debot")
    releaseImplementation("com.tomoima.debot:debot-no-op:$debot")

//==================== Test ====================
    testImplementation("junit:junit:4.12")
    testImplementation("com.nhaarman:mockito-kotlin:1.5.0")

    testImplementation("org.robolectric:robolectric:$robolectric")
    testImplementation("org.robolectric:shadows-multidex:$robolectric")

    testImplementation("com.willowtreeapps.assertk:assertk:0.9")
    testImplementation("org.threeten:threetenbp:1.3.3")

    androidTestImplementation("com.android.support.test:runner:1.0.1")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.1")
    androidTestImplementation("com.android.support.test.espresso:espresso-contrib:2.2.2")
    androidTestImplementation("com.willowtreeapps.assertk:assertk:0.9")
}

repositories {
    mavenCentral()
}

play {
    setTrack("beta") // 'production' or 'rollout' or 'beta' or 'alpha'
// userFraction = 0.1

}

ktlint {
    version = "0.14.0"
    android = true
    reporter = ReporterType.CHECKSTYLE
    ignoreFailures = true
}

deploygate {
    userName = "takahirom"
    token = System.getenv("DEPLOY_GATE_API_KEY")

    apks {
        create("release") {
            val hash = Runtime.getRuntime().exec("git rev-parse --short HEAD")
            message = "https://github.com/DroidKaigi/conference-app-2018/tree/${hash} ${System.getenv("CIRCLE_BUILD_URL")}"

            distributionKey = "aed2445665e27de6571227992d66ea489b6bdb44"
            releaseNote = "https://github.com/DroidKaigi/conference-app-2018/tree/${hash} ${System.getenv("CIRCLE_BUILD_URL")}"
        }
    }
}

apply(mapOf("plugin" to "com.google.gms.google-services"))
