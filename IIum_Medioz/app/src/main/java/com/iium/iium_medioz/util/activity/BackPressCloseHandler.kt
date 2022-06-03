package com.iium.iium_medioz.util.activity

import android.app.Activity
import androidx.core.app.ActivityCompat
import android.widget.Toast
import com.iium.iium_medioz.util.`object`.Constant

class BackPressCloseHandler(private val activity: Activity) : ActivityCompat() {
    private var toast: Toast? = null
    private var pressedTime: Long = 0

    fun onBackPressed() {
        if (System.currentTimeMillis() > pressedTime + Constant.BACKPRESS_CLOSE_TIME) {
            pressedTime = System.currentTimeMillis()
        } else if (System.currentTimeMillis() <= pressedTime + Constant.BACKPRESS_CLOSE_TIME) {
            finishAffinity(activity)
            toast!!.cancel()
        }
    }

    fun onBackPressed(listener: BackPressedListener) {
        if (System.currentTimeMillis() > pressedTime + Constant.BACKPRESS_CLOSE_TIME) {
            pressedTime = System.currentTimeMillis()
        } else if (System.currentTimeMillis() <= pressedTime + Constant.BACKPRESS_CLOSE_TIME) {
            listener.onBackPressFinish()
            toast!!.cancel()
        }
    }
}