package io.github.droidkaigi.confsched2018.data.repository

import android.content.Context
import io.github.droidkaigi.confsched2018.model.Staff
import io.github.droidkaigi.confsched2018.util.rx.SchedulerProvider
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.json.JSONObject
import java.io.IOException
import javax.inject.Inject

class StaffDataRepository @Inject constructor(
        private val context: Context,
        private val schedulerProvider: SchedulerProvider
) : StaffRepository {
    override fun loadStaff(): Completable = getStaff()
            .subscribeOn(schedulerProvider.computation())
            .toCompletable()

    override val staff: Flowable<List<Staff>>
        get() = getStaff().toFlowable().subscribeOn(schedulerProvider.computation())

    private fun getStaff(): Single<List<Staff>> {
        return Single.create { emitter ->
            try {
                val asset = loadJsonFromAsset()
                emitter.onSuccess(mapToEntity(asset))
            } catch (e: Exception) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }

    @Throws(RuntimeException::class)
    private fun mapToEntity(jsonStr: String): ArrayList<Staff> {
        val list = ArrayList<Staff>()
        val jsonObj = JSONObject(jsonStr)

        val data = jsonObj.getJSONArray("staff")

        (0 until data.length())
                .map { data.getJSONObject(it) }
                .mapTo(list) {
                    Staff().apply {
                        name = it.getString("name")
                        avatarUrl = "https://github.com/$name.png?size=100"
                        htmlUrl = "https://github.com/$name"
                    }
                }

        return list
    }

    @Throws(IOException::class)
    private fun loadJsonFromAsset(): String {
        val file = context.assets.open("staff.json")
        val buffer = ByteArray(file.available())
        file.read(buffer)
        file.close()
        return String(buffer)
    }
}
