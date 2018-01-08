package io.github.droidkaigi.confsched2018.data.db.fixeddata

import io.github.droidkaigi.confsched2018.data.api.response.mapper.LocalDateTimeAdapter
import io.github.droidkaigi.confsched2018.model.Room
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.model.parseDate
import io.github.droidkaigi.confsched2018.util.ext.toUnixMills

class SpecialSessions{
    companion object {

fun getSessions(): List<Session.SpecialSession> {
        var index = 0
        return listOf(
                Session.SpecialSession(
                        "100000" + index++,
                        "welcomeTalk",
                        1,
                        parseDate(
                                LocalDateTimeAdapter
                                        .parseDateString("2018-02-08T10:00:00")
                                        .toUnixMills()
                        ),
                        parseDate(
                                LocalDateTimeAdapter
                                        .parseDateString("2018-02-08T10:20:00")
                                        .toUnixMills()
                        ),
                        Room(513, "Hall")
                ),
                Session.SpecialSession(
                        "100000" + index++,
                        "lunch",
                        1,
                        parseDate(
                                LocalDateTimeAdapter.parseDateString("2018-02-08T11:50:00")
                                        .toUnixMills()
                        ),
                        parseDate(
                                LocalDateTimeAdapter.parseDateString("2018-02-08T10:20:00")
                                        .toUnixMills()
                        ),
                        Room(513, "Hall")
                ),
                Session.SpecialSession(
                        "100000" + index++,
                        "party",
                        1,
                        parseDate(
                                LocalDateTimeAdapter.parseDateString("2018-02-08T17:40:00")
                                        .toUnixMills()
                        ),
                        parseDate(
                                LocalDateTimeAdapter.parseDateString("2018-02-08T19:40:00")
                                        .toUnixMills()
                        ),
                        Room(513, "Hall")
                ),

                Session.SpecialSession(
                        "100000" + index++,
                        "lunch",
                        2,
                        parseDate(
                                LocalDateTimeAdapter.parseDateString("2018-02-09T11:50:00")
                                        .toUnixMills()
                        ),
                        parseDate(
                                LocalDateTimeAdapter.parseDateString("2018-02-09T12:50:00")
                                        .toUnixMills()
                        ),
                        Room(513, "Hall")
                ))
    }    }
}
