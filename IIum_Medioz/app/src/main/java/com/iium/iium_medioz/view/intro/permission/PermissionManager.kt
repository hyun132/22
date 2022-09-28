package com.iium.iium_medioz.view.intro.permission

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.ONE_PERMISSION_REQUEST_CODE

object PermissionManager {

    fun getPermissionGranted(context: Context?, permissionCode: String?): Boolean {
        return ContextCompat.checkSelfPermission(
            context!!,
            permissionCode!!
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getAllPermissionGranted(context: Context?): Boolean {
        var allGranted = true
        for (permissionCode in Constant.MUTILE_PERMISSION) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    permissionCode!!
                ) == PackageManager.PERMISSION_DENIED
            ) {
                allGranted = false
            }
        }
        return allGranted
    }

    fun requestAllPermissions(context: Context?) {
        val arraySize: Int = Constant.MUTILE_PERMISSION.size
        ActivityCompat.requestPermissions(
            (context as Activity?)!!,
            Constant.MUTILE_PERMISSION.toArray(arrayOfNulls<String>(arraySize)),
            Constant.ALL_PERMISSION_REQUEST_CODE
        )
    }

    fun requestPermissions(context: Context?, permissionCode: Array<String?>?) {
        ActivityCompat.requestPermissions(
            (context as Activity?)!!,
            permissionCode!!,
            ONE_PERMISSION_REQUEST_CODE
        )
    }

    fun requestMultiPermission(context: Context?, permissionCodes: Array<String?>?) {
        ActivityCompat.requestPermissions(
            (context as Activity?)!!,
            permissionCodes!!, ONE_PERMISSION_REQUEST_CODE
        )
    }
}