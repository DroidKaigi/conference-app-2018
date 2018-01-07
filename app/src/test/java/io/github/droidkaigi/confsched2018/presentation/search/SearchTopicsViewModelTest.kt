package io.github.droidkaigi.confsched2018.presentation.search

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.Topic
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
class SearchTopicsViewModelTest {

    @Mock private val repository: SessionRepository = mock()

    private lateinit var viewModel: SearchTopicsViewModel

    @Before fun init() {
        whenever(repository.refreshSessions()).doReturn(Completable.complete())
    }

    @Test fun topics_Empty() {
        whenever(repository.topics).doReturn(Flowable.empty())
        viewModel = SearchTopicsViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<List<Topic>>> = mock()

        viewModel.topics.observeForever(result)

        verify(repository).topics
        verify(result).onChanged(Result.inProgress())
    }

    @Test fun topics_Basic() {
        val topics = listOf(mock<Topic>())
        whenever(repository.topics).doReturn(Flowable.just(topics))
        viewModel = SearchTopicsViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<List<Topic>>> = mock()

        viewModel.topics.observeForever(result)

        verify(repository).topics
        verify(result).onChanged(Result.success(topics))
    }

    @Test fun topics_Error() {
        val runtimeException = RuntimeException("test")
        whenever(repository.topics).doReturn(Flowable.error(runtimeException))
        viewModel = SearchTopicsViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<List<Topic>>> = mock()

        viewModel.topics.observeForever(result)

        verify(repository).topics
        verify(result).onChanged(Result.failure(runtimeException.message!!, runtimeException))
    }
}