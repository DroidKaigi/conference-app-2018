package io.github.droidkaigi.confsched2018.presentation.sessions.item

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import io.github.droidkaigi.confsched2018.createDummySession
import io.github.droidkaigi.confsched2018.createDummySpecialSession
import io.github.droidkaigi.confsched2018.model.Date
import org.junit.Before
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(Enclosed::class)
class DateSessionsSectionTest {

    @RunWith(RobolectricTestRunner::class)
    abstract class BaseTest {
        lateinit var section: DateSessionsSection

        @Before fun init() {
            section = DateSessionsSection()
        }

        fun assertItemCount(expected: Int) {
            assert(section.itemCount).isEqualTo(expected)
        }

        fun <T : Any> assertItemInstanceOf(position: Int, expected: Class<T>) {
            assert(section.getItem(position)).isInstanceOf(expected)
        }

        fun assertDateHeaderPosition(by: Date, expected: Int) {
            assert(section.getDateHeaderPositionByDate(by)).isEqualTo(expected)
        }
    }

    class UpdateSessionsTest : BaseTest() {

        @Test fun emptySessions() {
            assertItemCount(0)
            section.updateSessions(emptyList(), {})
            assertItemCount(0)
        }

        @Test fun updateSessions() {
            assertItemCount(0)

            /*
            sections[0] : DateHeaderItem()
            sections[1] : Session()
             */
            section.updateSessions(listOf(createDummySession()), {})
            assertItemCount(2)
            assertItemInstanceOf(0, DateHeaderItem::class.java)
            assertItemInstanceOf(1, SpeechSessionItem::class.java)

            // re-update test
            section.updateSessions(listOf(createDummySession()), {})
            assertItemCount(2)
            assertItemInstanceOf(0, DateHeaderItem::class.java)
            assertItemInstanceOf(1, SpeechSessionItem::class.java)

            // include SpeechSession and SpecialSession
            /*
            sections[0] : DateHeaderItem()
            sections[1] : Session.SpecialSession()
            sections[2] : Session.SpeechSession()
             */
            section.updateSessions(listOf(
                    createDummySpecialSession(),
                    createDummySession()), {})
            assertItemCount(3)
            assertItemInstanceOf(0, DateHeaderItem::class.java)
            assertItemInstanceOf(1, SpecialSessionItem::class.java)
            assertItemInstanceOf(2, SpeechSessionItem::class.java)
        }
    }

    class GateHeaderPositionByDateTest : BaseTest() {

        @Test fun emptySessions() {
            assertDateHeaderPosition(Date(), 0)
            section.updateSessions(emptyList(), {})
            assertDateHeaderPosition(Date(), 0)
        }

        @Test fun existSessions() {
            /*
            sections[0] : DateHeaderItem(10000~10000)
            sections[1] : Session()
            sections[2] : Session()
            sections[3] : DateHeaderItem(20000~20000)
            sections[4] : Session()
            sections[5] : Session()
            sections[6] : DateHeaderItem(30000~30000)
            sections[7] : Session()
             */
            section.updateSessions(listOf(
                    createDummySession(startTime = 10000, endTime = 10000),
                    createDummySession(startTime = 10000, endTime = 10000),
                    createDummySession(startTime = 20000, endTime = 20000),
                    createDummySession(startTime = 20000, endTime = 20000),
                    createDummySession(startTime = 30000, endTime = 30000)
            ), {})

            assertItemCount(8)
            assertDateHeaderPosition(Date(1000), 0)
            assertDateHeaderPosition(Date(10000), 0)
            assertDateHeaderPosition(Date(10001), 3)
            assertDateHeaderPosition(Date(19999), 3)
            assertDateHeaderPosition(Date(20000), 3)
            assertDateHeaderPosition(Date(20001), 6)
            assertDateHeaderPosition(Date(29999), 6)
            assertDateHeaderPosition(Date(30000), 6)
            assertDateHeaderPosition(Date(30001), 6)
        }
    }
}
