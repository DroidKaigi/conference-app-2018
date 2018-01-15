package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.util.rx.TestSchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AllSessionsViewModelTest {
    @Mock private val repository: SessionRepository = mock()

    private lateinit var viewModel: AllSessionsViewModel

    @Before fun init() {
        whenever(repository.refreshSessions()).doReturn(Completable.complete())
    }

    @Test fun sessions_Empty() {
        whenever(repository.sessions).doReturn(Flowable.empty())
        viewModel = AllSessionsViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<List<Session>>> = mock()

        viewModel.sessions.observeForever(result)

        verify(repository).sessions
        verify(result).onChanged(Result.inProgress())
    }

    @Test fun sessions_Basic() {
        val sessions = listOf(mock<Session>())
        whenever(repository.sessions).doReturn(Flowable.just(sessions))
        viewModel = AllSessionsViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<List<Session>>> = mock()

        viewModel.sessions.observeForever(result)

        verify(repository).sessions
        verify(result).onChanged(Result.success(sessions))
    }

    @Test fun sessions_Error() {
        val runtimeException = RuntimeException("test")
        whenever(repository.sessions).doReturn(Flowable.error(runtimeException))
        viewModel = AllSessionsViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<List<Session>>> = mock()

        viewModel.sessions.observeForever(result)

        verify(repository).sessions
        verify(result).onChanged(Result.failure(runtimeException.message!!, runtimeException))
    }

    @Test fun favorite() {
        whenever(repository.favorite(any())).doReturn(Single.just(true))
        viewModel = AllSessionsViewModel(repository, TestSchedulerProvider())
        val session = mock<Session.SpeechSession>()

        viewModel.onFavoriteClick(session)

        verify(repository).favorite(session)
    }
}
