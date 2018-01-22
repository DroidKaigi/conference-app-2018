package io.github.droidkaigi.confsched2018.data.local

import io.github.droidkaigi.confsched2018.model.Staff
import org.json.JSONObject

object StaffJsonMapper {
    @Throws(RuntimeException::class)
    fun mapToStaffList(jsonStr: String): ArrayList<Staff> {
        val list = ArrayList<Staff>()
        val jsonObj = JSONObject(jsonStr)

        val data = jsonObj.getJSONArray("staff")

        (0 until data.length()).mapTo(list) {
            Staff().apply {
                name = data[it].toString()
                htmlUrl = "https://github.com/$name"
                avatarUrl = "$htmlUrl.png?size=100"
            }
        }

        return list
    }
}
