package io.github.droidkaigi.confsched2018.util.ext

import android.support.test.runner.AndroidJUnit4
import com.google.android.gms.tasks.Tasks
import org.junit.Test
import org.junit.runner.RunWith

/**
 * com.google.android.gms.tasks.tasks.OnCompleteListener called on Android's MainThread which
 * get with {@code Looper.getMainLooper()}.
 * So this test run as InstrumentationTest because this cannot run on JVM with that causes.
 */
@RunWith(AndroidJUnit4::class)
class FirebaseTaskExtKttest {
    @Test fun task_void_to_single() {
        // Tasks.whenAll() returns Task<Void>
        // Task<Void>.toSingle() always get NullPointerException
        Tasks.whenAll()
                .toSingle()
                .test()
                .await()
                .assertError(NullPointerException::class.java)
                .assertTerminated()
    }

    @Test fun task_string_to_single() {
        Tasks.forResult("test")
                .toSingle()
                .test()
                .await()
                .assertValueCount(1)
                .assertValue("test")
                .assertNoErrors()
                .assertComplete()
    }

    @Test fun task_void_to_complete() {
        Tasks.whenAll()
                .toCompletable()
                .test()
                .await()
                .assertNoErrors()
                .assertComplete()
    }

    @Test fun task_string_to_complete() {
        Tasks.forResult("test")
                .toCompletable()
                .test()
                .await()
                .assertNoValues()
                .assertNoErrors()
                .assertComplete()
    }
}
