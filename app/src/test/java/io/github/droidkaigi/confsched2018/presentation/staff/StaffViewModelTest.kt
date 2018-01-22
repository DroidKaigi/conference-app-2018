package io.github.droidkaigi.confsched2018.presentation.staff

import android.arch.lifecycle.Observer
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.github.droidkaigi.confsched2018.data.repository.StaffRepository
import io.github.droidkaigi.confsched2018.model.Staff
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
class StaffViewModelTest {
    @Mock private val repository: StaffRepository = mock()

    private lateinit var viewModel: StaffViewModel

    @Before fun init() {
        whenever(repository.loadStaff()).doReturn(Completable.complete())
    }

    @Test fun changeDataWhenLoadSucceeded() {
        // given
        val staff = mock<List<Staff>>()
        val result: Observer<Result<List<Staff>>> = mock()
        viewModel = StaffViewModel(repository, TestSchedulerProvider())

        // when
        whenever(repository.staff).doReturn(Flowable.just(staff))
        viewModel.staff.observeForever(result)

        // then
        verify(repository).staff
        verify(result).onChanged(Result.success(staff))
    }

    // TODO: implement error pattern
}
