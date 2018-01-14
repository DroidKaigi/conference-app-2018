package io.github.droidkaigi.confsched2018.presentation.sponsors

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.github.droidkaigi.confsched2018.data.api.response.mapper.ApplicationJsonAdapterFactory
import io.github.droidkaigi.confsched2018.data.api.response.mapper.LocalDateTimeAdapter
import io.github.droidkaigi.confsched2018.model.Sponsor
import io.github.droidkaigi.confsched2018.model.SponsorGroup
import io.github.droidkaigi.confsched2018.model.SponsorPlan
import io.github.droidkaigi.confsched2018.presentation.Result
import io.github.droidkaigi.confsched2018.presentation.common.mapper.toResult
import io.github.droidkaigi.confsched2018.util.ext.toLiveData
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.BackpressureStrategy
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import okhttp3.Request
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject
import io.github.droidkaigi.confsched2018.data.api.response.SponsorPlan as ResponsePlan

class SponsorsViewModel @Inject constructor(
        // FIXME inject sponsor repo.
        private val schedulerProvider: SchedulerProvider
) : ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    val sponsors: LiveData<Result<List<SponsorPlan>>> by lazy {
        Single.create<List<SponsorPlan>> {
            // FIXME inline to repo.
            val request = Request.Builder().apply {
                url("https://gist.githubusercontent.com/jmatsu/0025d16eb276bcafaf1a75760b38e265/raw/f7f0e7d9cd0669349d1a4698ae875a683bdbac70/sponsors.json")
            }.build()

            val body = OkHttpClient().newCall(request).execute().body()!!
            val moshi = Moshi.Builder()
                    .add(ApplicationJsonAdapterFactory.INSTANCE)
                    .add(LocalDateTime::class.java, LocalDateTimeAdapter())
                    .build()
            val plans = moshi.adapter<List<ResponsePlan>>(Types.newParameterizedType(List::class.java, ResponsePlan::class.java)).fromJson(body.source())!!
            it.onSuccess(plans.map {
                SponsorPlan(it.planName, when (it.planType) {
                    ResponsePlan.Type.PLATINUM -> SponsorPlan.Type.Platinum
                    ResponsePlan.Type.GOLD -> SponsorPlan.Type.Gold
                    ResponsePlan.Type.SILVER -> SponsorPlan.Type.Silver
                    ResponsePlan.Type.SUPPORTER -> SponsorPlan.Type.Supporter
                    ResponsePlan.Type.TECHNICAL_FOR_NETWORK -> SponsorPlan.Type.TechnicalForNetwork
                }, it.groups.map {
                    SponsorGroup(it.sponsors.map {
                        if (it.base64Img == it.imgUrl) {
                            Timber.d(it.link)
                        }
                        Sponsor(it.link, if (it.base64Img == null) Sponsor.ImageUri.NetworkImageUri(it.imgUrl!!) else Sponsor.ImageUri.Base64ImageUri(it.base64Img!!))
                    })
                })
            })
        }.subscribeOn(schedulerProvider.computation())
                .toResult(schedulerProvider)
                .toFlowable(BackpressureStrategy.BUFFER)
                .toLiveData()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}

