package com.iium.iium_medioz.util.calendar

import android.app.Application
import androidx.annotation.Px
import com.iium.iium_medioz.util.base.MyApplication.Companion.pixelRatio
import kotlin.math.roundToInt

class PixelRatio(private val app: Application) {
    private val displayMetrics
        get() = app.resources.displayMetrics

    val screenWidth
        get() = displayMetrics.widthPixels

    val screenHeight
        get() = displayMetrics.heightPixels

    val screenShort
        get() = screenWidth.coerceAtMost(screenHeight)

    val screenLong
        get() = screenWidth.coerceAtLeast(screenHeight)

    @Px
    fun toPixel(dp: Int) = (dp * displayMetrics.density).roundToInt()
    fun toPixelForFloatDp(dp: Float) = (dp * displayMetrics.density).roundToInt()
}

val Number.dp: Int
    get() = pixelRatio.toPixel(this.toInt())

val Number.dpFloat: Float
    get() = pixelRatio.toPixelForFloatDp(this.toFloat()).toFloat()