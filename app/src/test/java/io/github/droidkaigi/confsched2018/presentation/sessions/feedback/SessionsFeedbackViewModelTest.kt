package io.github.droidkaigi.confsched2018.presentation.sessions.feedback

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.DUMMY_SESSION_ID1
import io.github.droidkaigi.confsched2018.DUMMY_SESSION_ID2
import io.github.droidkaigi.confsched2018.DUMMY_SESSION_TITLE1
import io.github.droidkaigi.confsched2018.DUMMY_SESSION_TITLE2
import io.github.droidkaigi.confsched2018.createDummySessionFeedback
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
        val sessionFeedback_1 = createDummySessionFeedback(DUMMY_SESSION_ID1, DUMMY_SESSION_TITLE1)
        val sessionFeedback_2 = createDummySessionFeedback(DUMMY_SESSION_ID2, DUMMY_SESSION_TITLE2)
        val sessionFeedbackResults = listOf(sessionFeedback_1, sessionFeedback_2)
        whenever(repository.sessionFeedbacks).doReturn(Flowable.just(sessionFeedbackResults))
        viewModel = SessionsFeedbackViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<SessionFeedback>> = mock()
        viewModel.mutableSessionFeedback.observeForever(result)

        viewModel.sessionId = sessionFeedback_1.sessionId
        viewModel.sessionTitle = sessionFeedback_1.sessionTitle
        viewModel.init()

        verify(repository).sessionFeedbacks
        verify(result).onChanged(Result.success(sessionFeedback_1))
    }
}
