package io.github.droidkaigi.confsched2018.data.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "sponsor_plan")
data class SponsorPlanEntity(
        @PrimaryKey
        var id: Int,
        var name: String,
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
