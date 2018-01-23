package io.github.droidkaigi.confsched2018.test

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner

class TestAppRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?):
            Application = super.newApplication(cl, TestApp::class.java.name, context)

}
