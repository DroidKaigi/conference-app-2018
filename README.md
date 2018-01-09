# ![](app/src/main/res/mipmap-mdpi/ic_launcher.png) DroidKaigi 2018 official Android app 
[![CircleCI](https://circleci.com/gh/DroidKaigi/conference-app-2018.svg?style=svg&circle-token=b8e6a12e76295c24c7be8f57807cf6ab4139288e)](https://circleci.com/gh/DroidKaigi/conference-app-2018)[![Waffle.io - Columns and their card count](https://badge.waffle.io/4004fb95e3835aef9cfea229c8443b4b62eacf957879565e813f405126decfae.svg?columns=all)](https://waffle.io/DroidKaigi/conference-app-2018)

[DroidKaigi 2018](https://droidkaigi.jp/2018/en/) is a conference tailored for developers on 8th and 9th February 2018.

[<img src="https://dply.me/xt08ja/button/large" alt="Try it on your device via DeployGate">](https://dply.me/xt08ja#install)

# Features
TBD

# Contributing
We use [waffle.io](https://waffle.io/DroidKaigi/conference-app-2018) to manage tasks. If you'd like to contribute to the project but are not sure where to start off, please look for issues labelled [welcome contribute](https://github.com/DroidKaigi/conference-app-2018/labels/welcome%20contribute).

We've designated these issues as good candidates for easy contribution. You can always fork the repository and send a pull request (on a branch other than `master`).

We do accept suggestions for translations at [res/values-**/strings.xml](https://github.com/DroidKaigi/conference-app-2018/tree/master/app/src/main/res).

# Development Environment

## Kotlin
This app is full Kotlin!

## RxJava2 & LiveData
Converting RxJava2's publisher to AAC LiveData with [LiveDataReactiveStreams](https://developer.android.com/reference/android/arch/lifecycle/LiveDataReactiveStreams.html).

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

## Groupie

By using Groupie you can simplify the implementation around RecyclerView.

```kotlin
data class SpeakerItem(
        val speaker: Speaker
) : BindableItem<ItemSpeakerBinding>(speaker.id.hashCode().toLong()) {

    override fun bind(viewBinding: ItemSpeakerBinding, position: Int) {
        viewBinding.speaker = speaker
    }

    override fun getLayout(): Int = R.layout.item_speaker
}
```

# Architecture
This app uses an Android Architecture Components(AAC) based architecture using AAC(LiveData, ViewModel, Room), Kotlin, RxJava, DataBinding, dependency injection, Firebase.

<image src="https://user-images.githubusercontent.com/1386930/34318268-f8b7eece-e806-11e7-8b18-d9fc64dcd24e.png" width="500" />

## Fragment -> ViewModel

<image src="https://user-images.githubusercontent.com/1386930/34699651-ed4798b4-f521-11e7-84c9-11528a1c1f8c.png" width="500" />

Use `LifecycleObserver` for telling lifecycle to ViewModel.

SessionsFragment.kt

```kotlin
class SessionsFragment : Fragment(), Injectable {

    private lateinit var sessionsViewModel: SessionsViewModel

...
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ...
        lifecycle.addObserver(sessionsViewModel)
        ...
```

SessionsViewModel.kt

```kotlin
class SessionsViewModel @Inject constructor(
        private val repository: SessionRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel(), LifecycleObserver {
  ...
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
    ...
}
```

## ViewModel -> Repository
<image src="https://user-images.githubusercontent.com/1386930/34699666-0360c026-f522-11e7-935e-663006e72d01.png" width="500" />

Use RxJava2(RxKotlin) and `ViewModel#onCleared()` for preventing leaking.

SessionsViewModel.kt

```kotlin
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        repository
                .refreshSessions()
                .subscribeBy(onError = defaultErrorHandler())
                .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
```

## Repository -> API, Repository -> DB

<image src="https://user-images.githubusercontent.com/1386930/34699678-156f4f3a-f522-11e7-92a7-bebf96a9ed4b.png" width="500" />

Use Retrofit and save to the `Architecture Component Room`.

SessionDataRepository.kt

```kotlin
    override fun refreshSessions(): Completable {
        return api.getSessions()
                .doOnSuccess { response ->
                    sessionDatabase.save(response)
                }
                .subscribeOn(schedulerProvider.computation())
                .toCompletable()
    }
```

## DB -> Repository

<image src="https://user-images.githubusercontent.com/1386930/34699688-1eccee0c-f522-11e7-9c2c-77870cccca5b.png" width="500" />

Use `Room` with RxJava2 Flowable Support.
And SessionDataRepository holds Flowable property.

SessionDao.kt

```kotlin
    @Query("SELECT room_id, room_name FROM session GROUP BY room_id ORDER BY room_id")
    abstract fun getAllRoom(): Flowable<List<RoomEntity>>
```

SessionDataRepository.kt

```kotlin
class SessionDataRepository @Inject constructor(
        private val sessionDatabase: SessionDatabase,
...
) : SessionRepository {

    override val rooms: Flowable<List<Room>> =
            sessionDatabase.getAllRoom().toRooms()
```

## Repository -> ViewModel

<image src="https://user-images.githubusercontent.com/1386930/34699694-29ddfdcc-f522-11e7-83df-aa872eebafbb.png" width="500" />

We create LiveData from a ReactiveStreams publisher with [LiveDataReactiveStreams](https://developer.android.com/reference/android/arch/lifecycle/LiveDataReactiveStreams.html)


SessionsViewModel.kt

```kotlin
    val rooms: LiveData<Result<List<Room>>> by lazy {
        repository.rooms
                .toResult(schedulerProvider)
                .toLiveData()
    }
```

LiveDataReactiveStreamsExt.kt

```kotlin
fun <T> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this) as LiveData<T>
```

And using `Result` class for error handling with Kotlin extension.

```kotlin
fun <T> Flowable<T>.toResult(schedulerProvider: SchedulerProvider): Flowable<Result<T>> =
        compose { item ->
            item
                    .map { Result.success(it) }
                    .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
                    .observeOn(schedulerProvider.ui())
                    .startWith(Result.inProgress())
        }
```

```kotlin
sealed class Result<T>(val inProgress: Boolean) {
    class InProgress<T> : Result<T>(true)
    data class Success<T>(var data: T) : Result<T>(false)
    data class Failure<T>(val errorMessage: String?, val e: Throwable) : Result<T>(false)

```

## ViewModel -> Fragment

<image src="https://user-images.githubusercontent.com/1386930/34699699-320f9bb8-f522-11e7-9ce3-f02940f1343e.png" width="500" />

Fragment observe ViewModel's LiveData.
We can use the result with Kotlin `when` expression.
In `is Result.Success` block, you can access data with `result.data` by Kotlin Smart cast.

SessionsFragment.kt

```kotlin
        sessionsViewModel.rooms.observe(this, { result ->
            when (result) {
                is Result.InProgress -> {
                    binding.progress.show()
                }
                is Result.Success -> {
                    binding.progress.hide()
                    sessionsViewPagerAdapter.setRooms(result.data)
                }
                is Result.Failure -> {
                    Timber.e(result.e)
                    binding.progress.hide()
                }
            }
        })
```


## CREDIT
This project uses these codes.

Android Open Source Project  
EllipsizingTextView  
https://gist.github.com/stepango/1dcf6055a80f840f9185  
google/iosched  
