package io.github.droidkaigi.confsched2018.data.db

import android.arch.persistence.room.TypeConverter
import java.util.*

class Converters {
    companion object {
        @JvmStatic @TypeConverter
        fun fromTimestamp(value: Long?): Date? = if (value == null) null else Date(value)

        @JvmStatic @TypeConverter
        fun dateToTimestamp(date: Date?): Long? = date?.time
    }
}