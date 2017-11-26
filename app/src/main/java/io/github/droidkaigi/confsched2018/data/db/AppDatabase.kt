package io.github.droidkaigi.confsched2018.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import io.github.droidkaigi.confsched2018.data.entity.SessionEntity

@Database(entities = arrayOf(SessionEntity::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}