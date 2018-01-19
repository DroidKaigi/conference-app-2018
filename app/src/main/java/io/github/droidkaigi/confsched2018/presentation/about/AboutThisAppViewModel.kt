package io.github.droidkaigi.confsched2018.presentation.about

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import io.github.droidkaigi.confsched2018.data.repository.AboutThisAppDataRepository
import io.github.droidkaigi.confsched2018.model.AboutThisApp
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import javax.inject.Inject

class AboutThisAppViewModel @Inject constructor(
        private val repository: AboutThisAppDataRepository,
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {
    val aboutThisApps: LiveData<Result<List<AboutThisApp>>> by lazy {
        repository.aboutThisApps
                .toResult(schedulerProvider)
                .toLiveData()
    }
}
