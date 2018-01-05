package io.github.droidkaigi.confsched2018.presentation.search

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Speaker
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
class SearchSpeakersViewModelTest {
    @Mock private val repository: SessionRepository = mock()

    private lateinit var viewModel: SearchSpeakersViewModel

    @Before fun init() {
        whenever(repository.refreshSessions()).doReturn(Completable.complete())
    }

    @Test fun sessions_Empty() {
        whenever(repository.speakers).doReturn(Flowable.empty())
        viewModel = SearchSpeakersViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<List<Speaker>>> = mock()

        viewModel.speakers.observeForever(result)

        verify(repository).speakers
        verify(result).onChanged(Result.inProgress())
    }

    @Test fun sessions_Basic() {
        val speakers = listOf(mock<Speaker>())
        whenever(repository.speakers).doReturn(Flowable.just(speakers))
        viewModel = SearchSpeakersViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<List<Speaker>>> = mock()

        viewModel.speakers.observeForever(result)

        verify(repository).speakers
        verify(result).onChanged(Result.success(speakers))
    }

    @Test fun sessions_Error() {
        val runtimeException = RuntimeException("test")
        whenever(repository.speakers).doReturn(Flowable.error(runtimeException))
        viewModel = SearchSpeakersViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<List<Speaker>>> = mock()

        viewModel.speakers.observeForever(result)

        verify(repository).speakers
        verify(result).onChanged(Result.failure(runtimeException.message!!, runtimeException))
    }
}
