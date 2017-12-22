# ![](app/src/main/res/mipmap-mdpi/ic_launcher.png) DroidKaigi 2018 official Android app 
[![Build Status](https://www.bitrise.io/app/c0c3f0c3b3434a75/status.svg?token=8tTg_s4Konukhj_yKYDi0Q&branch=master)](https://www.bitrise.io/app/c0c3f0c3b3434a75)[![Waffle.io - Columns and their card count](https://badge.waffle.io/a55261b87ae865ad8c0b50404bb482b185fe3dcb034c5a644a007802094a92f5.svg?columns=all)](https://waffle.io/takahirom/conference-app-2018)

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
This app uses an Android Architecture Components(AAC) based architecture using AAC(LiveData, ViewModel, Room), Kotlin, RxJava, DataBinding, dependency injection.

<image src="https://user-images.githubusercontent.com/1386930/34080607-5b5f1caa-e384-11e7-99d9-b01c4f26b162.png" width="400" />


## CREDIT
Android Open Source Project  
EllipsizingTextView  
https://gist.github.com/stepango/1dcf6055a80f840f9185  
google/iosched  
