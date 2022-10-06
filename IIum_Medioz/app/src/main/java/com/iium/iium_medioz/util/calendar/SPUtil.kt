package com.iium.iium_medioz.util.calendar

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.iium.iium_medioz.util.calendar.SPUtil.Companion.SP_NAME
import javax.inject.Inject

interface SPUtil {
    val sharedPreferences: SharedPreferences
    var isFirstLaunch: Boolean
    var isOtherPlaceSelected: Boolean
    var lastSelectedLocationCode: Long

    companion object {
        const val SP_NAME = "DO_NOT_CHANGE_THIS"
    }
}

class SPUtilImpl @Inject constructor(context: Application) : SPUtil {
    override val sharedPreferences: SharedPreferences = context.getSharedPreferences(SP_NAME,
        Context.MODE_PRIVATE
    )

    override var isFirstLaunch: Boolean
        @SuppressLint("ApplySharedPref")
        get() {
            val result = sharedPreferences.getBoolean("isFirstLaunch", true)
            if (result) {
                sharedPreferences.edit().putBoolean("isFirstLaunch", false).commit()
            }
            return result
        }
        @SuppressLint("ApplySharedPref")
        set(_) {
            sharedPreferences.edit().putBoolean("isFirstLaunch", false).commit()
        }

    override var isOtherPlaceSelected: Boolean
        get() = sharedPreferences.getBoolean("isOtherPlaceSelected", false)
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putBoolean("isOtherPlaceSelected", value).commit()
        }

    override var lastSelectedLocationCode: Long
        get() = sharedPreferences.getLong("lastSelectedLocationCode", 0L)
        @SuppressLint("ApplySharedPref")
        set(value) {
            sharedPreferences.edit().putLong("lastSelectedLocationCode", value).commit()
        }

}