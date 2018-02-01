package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.ColumnInfo

data class SponsorPlanEntity(
        @ColumnInfo(name = "sponsor_plan_name")
        var name: String,
        @ColumnInfo(name = "sponsor_plan_type")
        var type: Type

) {
    enum class Type {
        PLATINUM,
        GOLD,
        SILVER,
        SUPPORTER,
        TECHNICAL_FOR_NETWORK
    }
}
