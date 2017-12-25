# ![](app/src/main/res/mipmap-mdpi/ic_launcher.png) DroidKaigi 2018 official Android app 
[![Build Status](https://www.bitrise.io/app/c0c3f0c3b3434a75/status.svg?token=8tTg_s4Konukhj_yKYDi0Q&branch=master)](https://www.bitrise.io/app/c0c3f0c3b3434a75)[![Waffle.io - Columns and their card count](https://badge.waffle.io/4004fb95e3835aef9cfea229c8443b4b62eacf957879565e813f405126decfae.svg?columns=all)](https://waffle.io/DroidKaigi/conference-app-2018)

[<img src="https://dply.me/xt08ja/button/large" alt="Try it on your device via DeployGate">](https://dply.me/xt08ja#install)
# Development Environment

## Kotlin
This app is full Kotlin!

## RxJava & LiveData
Converting RxJava's publisher to AAC LiveData with [LiveDataReactiveStreams](https://developer.android.com/reference/android/arch/lifecycle/LiveDataReactiveStreams.html).

> AllSessionsViewModel.kt

```kotlin
repository.sessions
    .toResult(schedulerProvider)
    .toLiveData()
```

> LiveDataReactiveStreamsExt.kt

```kotlin
fun <T> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this)
```

# Architecture
This app uses an Android Architecture Components(AAC) based architecture using AAC(LiveData, ViewModel, Room), Kotlin, RxJava, DataBinding, dependency injection, Firebase.

<image src="https://user-images.githubusercontent.com/1386930/34318268-f8b7eece-e806-11e7-8b18-d9fc64dcd24e.png" width="500" />


## CREDIT
Android Open Source Project  
EllipsizingTextView  
https://gist.github.com/stepango/1dcf6055a80f840f9185  
google/iosched  
