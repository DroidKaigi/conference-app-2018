package io.github.droidkaigi.confsched2018.util.ext

import android.support.constraint.ConstraintLayout
import android.support.constraint.ConstraintSet

fun ConstraintLayout.clone(): ConstraintSet = ConstraintSet().apply { clone(this) }

