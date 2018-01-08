package io.github.droidkaigi.confsched2018.presentation.speaker

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.Speaker
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
class SpeakerDetailViewModelTest {
    @Mock private val repository: SessionRepository = mock()

    private lateinit var viewModel: SpeakerDetailViewModel

    @Before fun init() {
        whenever(repository.refreshSessions()).doReturn(Completable.complete())
    }

    @Test fun sessions_Empty() {
        whenever(repository.speakerSessions).doReturn(Flowable.empty())
        viewModel = SpeakerDetailViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<Pair<Speaker, List<Session.SpeechSession>>>> = mock()

        viewModel.speakerSessions.observeForever(result)

        verify(repository).speakerSessions
        verify(result).onChanged(Result.inProgress())
    }

    @Test fun sessions_Basic() {
        val speaker = mock<Speaker>()
        val sessions = listOf(mock<Session.SpeechSession>())
        val speakerToSessions = speaker to sessions
        whenever(speaker.id).doReturn("hoge")
        val speakerSessions = mapOf(speaker to sessions)
        whenever(repository.speakerSessions).doReturn(Flowable.just(speakerSessions))
        viewModel = SpeakerDetailViewModel(repository, TestSchedulerProvider())
        viewModel.speakerId = "hoge"
        val result: Observer<Result<Pair<Speaker, List<Session.SpeechSession>>>> = mock()

        viewModel.speakerSessions.observeForever(result)

        verify(repository).speakerSessions
        verify(result).onChanged(Result.inProgress())
        verify(result).onChanged(Result.success(speakerToSessions))
    }

    @Test fun sessions_Error() {
        val runtimeException = RuntimeException("test")
        whenever(repository.speakerSessions).doReturn(Flowable.error(runtimeException))
        viewModel = SpeakerDetailViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<Pair<Speaker, List<Session.SpeechSession>>>> = mock()

        viewModel.speakerSessions.observeForever(result)

        verify(repository).speakerSessions
        verify(result).onChanged(Result.failure(runtimeException.message!!, runtimeException))
    }

    @Test fun favorite() {
        whenever(repository.favorite(any())).doReturn(Single.just(true))
        viewModel = SpeakerDetailViewModel(repository, TestSchedulerProvider())
        val session = mock<Session.SpeechSession>()

        viewModel.onFavoriteClick(session)

        verify(repository).favorite(session)
    }
}
