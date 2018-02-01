package io.github.droidkaigi.confsched2018.data.db.entity.mapper

import android.arch.persistence.room.TypeConverter
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorPlanEntity
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

object Converters {
    @JvmStatic
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        if (value == null) {
            return null
        }
        return LocalDateTime.ofInstant(
                Instant.ofEpochSecond(value), ZoneId.of("JST", ZoneId.SHORT_IDS))
    }

    @JvmStatic
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? =
            date?.atZone(ZoneId.of("JST", ZoneId.SHORT_IDS))?.toEpochSecond()

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
