package io.github.droidkaigi.confsched2018.util

import timber.log.Timber

fun defaultErrorHandler(): (Throwable) -> Unit = { e -> Timber.e(e) }
