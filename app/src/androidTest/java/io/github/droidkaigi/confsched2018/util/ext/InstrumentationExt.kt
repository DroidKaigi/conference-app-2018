package io.github.droidkaigi.confsched2018.util.ext

import android.app.Activity
import android.app.Instrumentation
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import android.support.test.runner.lifecycle.Stage

// https://stackoverflow.com/questions/24517291/get-current-activity-in-espresso-android
fun Instrumentation.currentActivity(): Activity? {
    var activity: Activity? = null
    runOnMainSync {
        val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance()
                .getActivitiesInStage(Stage.RESUMED)
        if (resumedActivities.iterator().hasNext()) {
            activity = resumedActivities.iterator().next()
        }
    }
    return activity
}
