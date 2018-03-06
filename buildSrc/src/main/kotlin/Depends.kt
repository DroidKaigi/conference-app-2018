object Depends {
    object Kotlin {
        val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    }

    object Coroutine {
        val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinx_coroutines}"
        val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinx_coroutines}"
    }

    object Support {
        val support_v4 = "com.android.support:support-v4:${Versions.supportLibrary}"
        val appcompat_v7 = "com.android.support:appcompat-v7:${Versions.supportLibrary}"
        val design = "com.android.support:design:${Versions.supportLibrary}"
        val cardview_v7 = "com.android.support:cardview-v7:${Versions.supportLibrary}"
        val customtabs = "com.android.support:customtabs:${Versions.supportLibrary}"
        val constraint = "com.android.support.constraint:constraint-layout:1.1.0-beta4"
        val multidex = "com.android.support:multidex:1.0.2"
        val support_emoji = "com.android.support:support-emoji-appcompat:${Versions.supportLibrary}"
        val preference_v7 = "com.android.support:preference-v7:${Versions.supportLibrary}"
        val preference_v14 = "com.android.support:preference-v14:${Versions.supportLibrary}"
    }
    
    val ktx = "androidx.core:core-ktx:0.1"

    object OkHttp3 {
        val loggingIntercepter = "com.squareup.okhttp3:logging-interceptor:3.9.1"
    }

    object Retrofit {
        val core = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
        val converterMoshi = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
        val adapterRxJava2 = "com.squareup.retrofit2:adapter-rxjava2:${Versions.retrofit}"
        val adapterCoroutine = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-experimental-adapter:1.0.0"
    }

    object Kotshi {
        val api = "se.ansman.kotshi:api:${Versions.kotshi}"
        val compiler = "se.ansman.kotshi:compiler:${Versions.kotshi}"
    }

    object LifeCycle {
        val runtime = "android.arch.lifecycle:runtime:${Versions.archLifecycle}"
        val extensions = "android.arch.lifecycle:extensions:${Versions.archLifecycle}"
        val reactivestreams = "android.arch.lifecycle:reactivestreams:${Versions.archLifecycle}"
    }

    object Room {
        val runtime = "android.arch.persistence.room:runtime:${Versions.archRoom}"
        val rxjava2 = "android.arch.persistence.room:rxjava2:${Versions.archRoom}"
        val compiler = "android.arch.persistence.room:compiler:${Versions.archRoom}"
    }

    object RxJava2 {
        val core = "io.reactivex.rxjava2:rxjava:2.1.9"
        val android = "io.reactivex.rxjava2:rxandroid:2.0.1"
        val kotlin = "io.reactivex.rxjava2:rxkotlin:2.2.0"
    }

    val rxbroadcast = "com.cantrowitz:rxbroadcast:2.0.0"

    object Binding {
        val compiler = "com.android.databinding:compiler:3.0.1"
    }

    val rxrelay = "com.jakewharton.rxrelay2:rxrelay:2.0.0"

    object Dagger {
        val core = "com.google.dagger:dagger:${Versions.dagger}"
        val compiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
        val android = "com.google.dagger:dagger-android:${Versions.dagger}"
        val androidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
        val androidProcessor = "com.google.dagger:dagger-android-processor:${Versions.dagger}"
    }

    object PlayService {
        val map = "com.google.android.gms:play-services-maps:${Versions.firebase}"
        val oss = "com.google.android.gms:play-services-oss-licenses:${Versions.firebase}"
    }

    object Firebase {
        val firestore = "com.google.firebase:firebase-firestore:${Versions.firebase}"
        val auth = "com.google.firebase:firebase-auth:${Versions.firebase}"
        val core = "com.google.firebase:firebase-core:${Versions.firebase}"
        val messaging = "com.google.firebase:firebase-messaging:${Versions.firebase}"
    }

    val threetenabp = "com.jakewharton.threetenabp:threetenabp:1.0.5"

    object Kotpref {
        val kotpref = "com.chibatching.kotpref:kotpref:${Versions.kotpref}"
        val initializer = "com.chibatching.kotpref:initializer:${Versions.kotpref}"
        val enumSupport = "com.chibatching.kotpref:enum-support:${Versions.kotpref}"
    }

    object Glide {
        val core = "com.github.bumptech.glide:glide:${Versions.glide}"
        val okhttp3 = "com.github.bumptech.glide:okhttp3-integration:${Versions.glide}"
        val compiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    }

    object Groupie {
        val core = "com.xwray:groupie:${Versions.groupie}"
        val binding = "com.xwray:groupie-databinding:${Versions.groupie}"
    }

    val downloadableCalligraphy = "com.github.takahirom.downloadable.calligraphy:downloadable-calligraphy:0.1.2"

    object Stetho {
        val core = "com.facebook.stetho:stetho:${Versions.stetho}"
        val okhttp3 = "com.facebook.stetho:stetho-okhttp3:${Versions.stetho}"
    }

    val crashlytics = "com.crashlytics.sdk.android:crashlytics:2.8.0@aar"
    val timber = "com.jakewharton.timber:timber:4.6.0"
    val leakcanary = "com.squareup.leakcanary:leakcanary-android:1.5.4"

    object Debot {
        val core = "com.tomoima.debot:debot:${Versions.debot}"
        val noop = "com.tomoima.debot:debot-no-op:${Versions.debot}"
    }

    val junit = "junit:junit:4.12"
    val mockito = "org.mockito:mockito-core:2.15.0"
    val mockitoKotlin = "com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-alpha03"

    object Robolectric {
        val core = "org.robolectric:robolectric:${Versions.robolectric}"
        val multidex = "org.robolectric:shadows-multidex:${Versions.robolectric}"
    }

    val assertk = "com.willowtreeapps.assertk:assertk:0.9"
    val threetenbp = "org.threeten:threetenbp:1.3.6"

    object SupportTest {
        val runner = "com.android.support.test:runner:1.0.1"
        val espresso = "com.android.support.test.espresso:espresso-core:3.0.1"
        val contrib = "com.android.support.test.espresso:espresso-contrib:3.0.1"

    }


}

