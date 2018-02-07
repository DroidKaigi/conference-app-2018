package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import android.arch.persistence.room.TypeConverter
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanEntity
import org.threeten.bp.Instant

object Converters {
    @JvmStatic
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? =
            if (value == null) {
                null
            } else {
                Instant.ofEpochSecond(value)
            }

    @JvmStatic
    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? =
            date?.epochSecond

    @JvmStatic
    @TypeConverter
    fun fromSponsorType(value: String?): SponsorPlanEntity.Type? {
        return SponsorPlanEntity.Type.values().firstOrNull { it.name == value }
    }

    @JvmStatic
    @TypeConverter
    fun sponsorTypeToString(type: SponsorPlanEntity.Type?): String? {
        return type?.name
    }
}
