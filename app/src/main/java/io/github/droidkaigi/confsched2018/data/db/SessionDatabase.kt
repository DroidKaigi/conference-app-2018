package io.github.droidkaigi.confsched2018.data.db

import io.github.droidkaigi.confsched2018.data.api.response.Response
import io.github.droidkaigi.confsched2018.data.db.entity.RoomEntity
import io.github.droidkaigi.confsched2018.data.db.entity.SessionWithSpeakers
import io.github.droidkaigi.confsched2018.data.db.entity.SpeakerEntity
import io.github.droidkaigi.confsched2018.data.db.entity.TopicEntity
import io.reactivex.Flowable

interface SessionDatabase {
    fun getAllSessions(): Flowable<List<SessionWithSpeakers>>
    fun getAllSpeaker(): Flowable<List<SpeakerEntity>>
    fun getAllRoom(): Flowable<List<RoomEntity>>
    fun getAllTopic(): Flowable<List<TopicEntity>>
    fun save(response: Response)
}
