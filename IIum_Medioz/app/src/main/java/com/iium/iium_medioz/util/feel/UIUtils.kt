package com.iium.iium_medioz.util.feel

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.media.MediaMetadataRetriever
import android.provider.Settings
import android.util.TypedValue
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import com.iium.iium_medioz.R
import java.io.File
import kotlin.math.roundToInt


fun ImageView.getFeeling(code: Int) {
    val emotes = listOf(
        R.drawable.icon_happy,
        R.drawable.icon_angry,
        R.drawable.icon_sad,
        R.drawable.icon_thanks,
        R.drawable.icon_surprise,
    )
    setImageResource(emotes[code])
}

fun TextView.getFeelingStatus(code: Int) {
    val emotes = listOf(
        "행복",
        "화남",
        "슬픔",
        "감사",
        "놀람",
    )
    text = emotes[code]
}

@SuppressLint("HardwareIds")
fun Context.getDeviceId(): String = Settings.Secure.getString(
    contentResolver,
    Settings.Secure.ANDROID_ID
)

val Int.dp: Int
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).roundToInt()

fun View.hide() {
    visibility = GONE
}

fun View.show() {
    visibility = VISIBLE
}

fun getDuration(file: File): String {
    val mediaMetadataRetriever = MediaMetadataRetriever()
    mediaMetadataRetriever.setDataSource(file.absolutePath)
    val durationStr =
        mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
    return formateMilliSeccond(durationStr?.toLong()!!)
}

fun getDuration(url: String): String {
    return try {
        val mediaMetadataRetriever = MediaMetadataRetriever()
        mediaMetadataRetriever.setDataSource(url)
        val durationStr =
            mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        formateMilliSeccond(durationStr?.toLong()!!)
    } catch (e: Exception) {
        "00:00"
    }
}

fun formateMilliSeccond(milliseconds: Long): String {
    var finalTimerString = ""
    var secondsString = ""

    // Convert total duration into time
    val hours = (milliseconds / (1000 * 60 * 60)).toInt()
    val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
    val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()

    // Add hours if there
    if (hours > 0) {
        finalTimerString = "$hours:"
    }

    // Prepending 0 to seconds if it is one digit
    secondsString = if (seconds < 10) {
        "0$seconds"
    } else {
        "" + seconds
    }
    finalTimerString = "$finalTimerString$minutes:$secondsString"
    return finalTimerString
}

fun getColorSave(id: Int): Int {
    val colors = mutableListOf(
        R.color.white,
        R.color.biruJurnal,
        R.color.kuningJurnal,
        R.color.pinkJurnal
    )
    return colors[id]
}