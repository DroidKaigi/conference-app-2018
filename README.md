# conference-app-2018
[![Build Status](https://www.bitrise.io/app/c0c3f0c3b3434a75/status.svg?token=8tTg_s4Konukhj_yKYDi0Q&branch=master)](https://www.bitrise.io/app/c0c3f0c3b3434a75)

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
