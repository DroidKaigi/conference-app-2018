package io.github.droidkaigi.confsched2018.presentation.sessions

import android.arch.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.droidkaigi.confsched2018.model.Session
import io.github.droidkaigi.confsched2018.presentation.action.SessionChangedAction
import io.github.droidkaigi.confsched2018.presentation.dispacher.Dispatcher
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SessionsStoreTest {
    @Mock private val dispatcher: Dispatcher = spy(Dispatcher())

    private lateinit var store: SessionsStore

    @Test fun sessions_Empty() {
        val channel = Channel<SessionChangedAction>()
        whenever(dispatcher.asChannel<SessionChangedAction>()).doReturn(channel)
        store = SessionsStore(dispatcher)
        val result: Observer<List<Session>> = mock()

        store.sessions.observeForever(result)

        verify(result, never()).onChanged(any())
    }

    @Test fun sessions_Basic() {
        val sessions = listOf(mock<Session>())
        val channel = Channel<SessionChangedAction>()
        whenever(dispatcher.asChannel<SessionChangedAction>()).doReturn(channel)
        store = SessionsStore(dispatcher)
        val result: Observer<List<Session>> = mock()
        store.sessions.observeForever(result)

        runBlocking {
            channel.send(SessionChangedAction(sessions))
        }

        verify(result).onChanged(sessions)
    }

}
