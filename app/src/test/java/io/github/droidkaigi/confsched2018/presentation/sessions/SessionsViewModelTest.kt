package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.SessionSchedule
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.util.TestLifecycleOwner
import io.github.droidkaigi.confsched2018.util.rx.TestSchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SessionsViewModelTest {
    @Mock private val repository: SessionRepository = mock()

    private lateinit var viewModel: SessionsViewModel

    @Before fun init() {
        whenever(repository.refreshSessions()).doReturn(Completable.complete())
    }

    @Test fun sessions_rooms_Empty() {
        whenever(repository.rooms).doReturn(Flowable.empty())
        viewModel = SessionsViewModel(repository, TestSchedulerProvider())
        viewModel.changeTabMode(SessionTabMode.ROOM)
        val result: Observer<Result<SessionTab>> = mock()

        viewModel.tab.observeForever(result)

        verify(repository).rooms
        verify(result).onChanged(Result.inProgress())
    }

    @Test fun sessions_rooms_Basic() {
        val rooms = listOf(mock<Room>())
        whenever(repository.rooms).doReturn(Flowable.just(rooms))
        viewModel = SessionsViewModel(repository, TestSchedulerProvider())
        viewModel.changeTabMode(SessionTabMode.ROOM)
        val result: Observer<Result<SessionTab>> = mock()

        viewModel.tab.observeForever(result)

        verify(repository).rooms
        verify(result).onChanged(Result.success(SessionTab.Room(rooms)))
    }

    @Test fun sessions_rooms_Error() {
        val runtimeException = RuntimeException("test")
        whenever(repository.rooms).doReturn(Flowable.error(runtimeException))
        viewModel = SessionsViewModel(repository, TestSchedulerProvider())
        viewModel.changeTabMode(SessionTabMode.ROOM)
        val result: Observer<Result<SessionTab>> = mock()

        viewModel.tab.observeForever(result)

        verify(repository).rooms
        verify(result).onChanged(Result.failure(runtimeException.message!!, runtimeException))
    }


    @Test fun sessions_times_Empty() {
        whenever(repository.schedules).doReturn(Flowable.empty())
        viewModel = SessionsViewModel(repository, TestSchedulerProvider())
        viewModel.changeTabMode(SessionTabMode.SCHEDULE)
        val result: Observer<Result<SessionTab>> = mock()

        viewModel.tab.observeForever(result)

        verify(repository).schedules
        verify(result).onChanged(Result.inProgress())
    }

    @Test fun sessions_times_Basic() {
        val schedules = listOf(mock<SessionSchedule>())
        whenever(repository.schedules).doReturn(Flowable.just(schedules))
        viewModel = SessionsViewModel(repository, TestSchedulerProvider())
        viewModel.changeTabMode(SessionTabMode.SCHEDULE)
        val result: Observer<Result<SessionTab>> = mock()

        viewModel.tab.observeForever(result)

        verify(repository).schedules
        verify(result).onChanged(Result.success(SessionTab.Schedule(schedules)))
    }

    @Test fun sessions_times_Error() {
        val runtimeException = RuntimeException("test")
        whenever(repository.schedules).doReturn(Flowable.error(runtimeException))
        viewModel = SessionsViewModel(repository, TestSchedulerProvider())
        viewModel.changeTabMode(SessionTabMode.SCHEDULE)
        val result: Observer<Result<SessionTab>> = mock()

        viewModel.tab.observeForever(result)

        verify(repository).schedules
        verify(result).onChanged(Result.failure(runtimeException.message!!, runtimeException))
    }

    @Test fun onCreate() {
        whenever(repository.refreshSessions()).doReturn(Completable.complete())
        whenever(repository.rooms).doReturn(Flowable.empty())
        val testLifecycleOwner = TestLifecycleOwner()
        viewModel = SessionsViewModel(repository, TestSchedulerProvider())
        testLifecycleOwner.lifecycle.addObserver(viewModel)

        testLifecycleOwner.handleEvent(Lifecycle.Event.ON_CREATE)

        verify(repository).refreshSessions()
    }
}
