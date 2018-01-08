package io.github.droidkaigi.confsched2018.presentation.topic

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.Topic
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
class TopicDetailViewModelTest {

    @Mock private val repository: SessionRepository = mock()

    private lateinit var viewModel: TopicDetailViewModel

    @Before fun init() {
        whenever(repository.refreshSessions()).doReturn(Completable.complete())
    }

    @Test fun sessions_Empty() {
        whenever(repository.topicSessions).doReturn(Flowable.empty())
        viewModel = TopicDetailViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<Pair<Topic, List<Session.SpeechSession>>>> = mock()

        viewModel.topicSessions.observeForever(result)

        verify(repository).topicSessions
        verify(result).onChanged(Result.inProgress())
    }

    @Test fun sessions_Basic() {
        val topic = mock<Topic>()
        val sessions = listOf(mock<Session.SpeechSession>())
        val topicToSessions = topic to sessions
        whenever(topic.id).doReturn(1234)
        val topicSessions = mapOf(topic to sessions)
        whenever(repository.topicSessions).doReturn(Flowable.just(topicSessions))
        viewModel = TopicDetailViewModel(repository, TestSchedulerProvider())
        viewModel.topicId = 1234
        val result: Observer<Result<Pair<Topic, List<Session.SpeechSession>>>> = mock()

        viewModel.topicSessions.observeForever(result)

        verify(repository).topicSessions
        verify(result).onChanged(Result.inProgress())
        verify(result).onChanged(Result.success(topicToSessions))
    }

    @Test fun sessions_Error() {
        val runtimeException = RuntimeException("test")
        whenever(repository.topicSessions).doReturn(Flowable.error(runtimeException))
        viewModel = TopicDetailViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<Pair<Topic, List<Session.SpeechSession>>>> = mock()

        viewModel.topicSessions.observeForever(result)

        verify(repository).topicSessions
        verify(result).onChanged(Result.failure(runtimeException.message!!, runtimeException))
    }

    @Test fun favorite() {
        whenever(repository.favorite(any())).doReturn(Single.just(true))
        viewModel = TopicDetailViewModel(repository, TestSchedulerProvider())
        val session = mock<Session.SpeechSession>()

        viewModel.onFavoriteClick(session)

        verify(repository).favorite(session)
    }
}
