package com.iium.iium_medioz.util.activity

import android.view.View

class OnSingleClickListener (private val onSingleClick: (View) -> Unit) : View.OnClickListener {

    companion object {
        const val CLICK_INTERVAL = 500
    }

    private var lastClickedTime: Long = 0L

    private fun isSafe(): Boolean {
        return System.currentTimeMillis() - lastClickedTime > CLICK_INTERVAL
    }

    override fun onClick(v: View?) {
        if (isSafe() && v != null) {
            onSingleClick(v)
        }
        lastClickedTime = System.currentTimeMillis()
    }
}

fun View.setOnSingleClickListener(block: () -> Unit) {
    val popupClickListener = OnSingleClickListener {
        block()
    }
    setOnClickListener(popupClickListener)
}