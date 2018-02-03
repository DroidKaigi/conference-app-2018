package io.github.droidkaigi.confsched2018.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import io.github.droidkaigi.confsched2018.data.db.dao.ContributorDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionFeedbackDao
import io.github.droidkaigi.confsched2018.data.db.dao.SessionSpeakerJoinDao
import io.github.droidkaigi.confsched2018.data.db.dao.SpeakerDao
import io.github.droidkaigi.confsched2018.data.db.dao.SponsorDao
import io.github.droidkaigi.confsched2018.data.db.entity.ContributorEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionFeedbackEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionSpeakerJoinEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SponsorEntity
import io.github.droidkaigi.confsched2018.data.db.entity.mapper.Converters

@Database(
        entities = [
            (ContributorEntity::class),
            (SessionEntity::class),
            (SpeakerEntity::class),
            (SessionSpeakerJoinEntity::class),
            (SessionFeedbackEntity::class),
            (SponsorEntity::class)
        ],
        version = 9
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contributorDao(): ContributorDao
    abstract fun sessionDao(): SessionDao
    abstract fun speakerDao(): SpeakerDao
    abstract fun sponsorDao(): SponsorDao
    abstract fun sessionSpeakerDao(): SessionSpeakerJoinDao
    abstract fun sessionFeedbackDao(): SessionFeedbackDao
}
