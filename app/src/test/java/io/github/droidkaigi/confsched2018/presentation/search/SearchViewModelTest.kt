package io.github.droidkaigi.confsched2018.presentation.search

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.createDummySessions
import io.github.droidkaigi.confsched2018.createDummySpeaker
import io.github.droidkaigi.confsched2018.data.repository.SessionRepository
import io.github.droidkaigi.confsched2018.model.SearchResult
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.util.rx.TestSchedulerProvider
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchViewModelTest {
    @Mock private val repository: SessionRepository = mock()

    private lateinit var viewModel: SearchViewModel

    @Before fun init() {
        whenever(repository.refreshSessions()).doReturn(Completable.complete())
    }

    @Test fun search_Empty() {
        whenever(repository.search("query")).doReturn(Single.just(SearchResult(listOf(), listOf())))
        viewModel = SearchViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<SearchResult>> = mock()
        viewModel.result.observeForever(result)

        viewModel.onQuery("query")

        verify(repository).search("query")
        verify(result).onChanged(Result.inProgress())
        verify(result).onChanged(Result.success(SearchResult(listOf(), listOf())))
    }

    @Test fun search_Basic() {
        val searchResult = SearchResult(createDummySessions(), listOf(createDummySpeaker()))
        whenever(repository.search("query")).doReturn(Single.just(searchResult))
        viewModel = SearchViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<SearchResult>> = mock()
        viewModel.result.observeForever(result)

        viewModel.onQuery("query")

        verify(repository).search("query")
        verify(result).onChanged(Result.success(searchResult))
    }

    @Test fun search_Error() {
        val runtimeException = RuntimeException("test")
        whenever(repository.search("query")).doReturn(Single.error(runtimeException))
        viewModel = SearchViewModel(repository, TestSchedulerProvider())
        val result: Observer<Result<SearchResult>> = mock()
        viewModel.result.observeForever(result)

        viewModel.onQuery("query")

        verify(repository).search("query")
        verify(result).onChanged(Result.failure(runtimeException.message!!, runtimeException))
    }

    @Test fun favorite() {
        whenever(repository.favorite(any())).doReturn(Single.just(true))
        viewModel = SearchViewModel(repository, TestSchedulerProvider())
        val session = mock<Session.SpeechSession>()

        viewModel.onFavoriteClick(session)

        verify(repository).favorite(session)
    }
}
