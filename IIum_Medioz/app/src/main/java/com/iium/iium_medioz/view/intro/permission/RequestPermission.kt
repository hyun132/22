package com.iium.iium_medioz.view.intro.permission

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build


const val PERMISSION_REQUEST_CODE = 91248578

/**
 * 전화 권한을 체크하고 원한 요청을 한다.
 * @return 권한이 허용되어 있으면 true 아니면 false
 */
fun requestReadPhoneNumbersPermission(activity: Activity): Boolean {
    //전화 권한
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        if (activity.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            activity.requestPermissions(
                arrayOf(Manifest.permission.CAMERA),
                PERMISSION_REQUEST_CODE
            )
            return false
        }
    }

    return true
}

/**
 * 전화부 권한을 체크하고 원한 요청을 한다.
 * @return 권한이 허용되어 있으면 true 아니면 false
 */
fun requestReadContactsPermission(activity: Activity): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return true
    }

    // 전화부 권한
    if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
        activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
        activity.requestPermissions(
            arrayOf(Manifest.permission.READ_CONTACTS),
            PERMISSION_REQUEST_CODE
        )
        return false
    }

    return true
}

/**
 * 저정공간 권한을 체크하고 원한 요청을 한다.
 * @return 권한이 허용되어 있으면 true 아니면 false
 */
fun requestStoragePermission(activity: Activity): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        return true
    }

    // 저장공간 권한
    if (activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED ||
        activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED
    ) {
        activity.requestPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PERMISSION_REQUEST_CODE
        )
        return false
    }

    return true
}