package io.github.droidkaigi.confsched2018.presentation.sessions.feedback

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.createDummySessionFeedback
import io.github.droidkaigi.confsched2018.createDummySessions
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.SessionFeedback
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.util.rx.TestSchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SessionsFeedbackViewModelTest {
    @Mock private val repository: SessionRepository = mock()

    private lateinit var viewModel: SessionsFeedbackViewModel

    @Before fun init() {
        whenever(repository.refreshSessions()).doReturn(Completable.complete())
    }

    @Test fun init_Basic() {
        val sessions = createDummySessions()
        whenever(repository.sessions).doReturn(Flowable.just(sessions))
        viewModel = SessionsFeedbackViewModel(repository, TestSchedulerProvider())
        viewModel.sessionId = sessions[0].id
        val result: Observer<Result<SessionFeedback>> = mock()
        viewModel.sessionFeedback.observeForever(result)


        verify(repository).sessions
        verify(result).onChanged(Result.inProgress())
        verify(result).onChanged(Result.success(
                createDummySessionFeedback(
                        sessions[0].id
                )
        ))
    }
}
