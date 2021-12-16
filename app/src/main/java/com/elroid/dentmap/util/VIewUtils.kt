package com.elroid.dentmap.util

import android.content.res.Resources
import kotlin.math.round

fun Int.dpToPx(): Int = round(toFloat().dpToPx()).toInt()

fun Float.dpToPx(): Float = this * Resources.getSystem().displayMetrics.density