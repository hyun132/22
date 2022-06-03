package com.iium.iium_medioz.view.intro.permission

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.databinding.ActivityPerMissionBinding
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.view.intro.splash.SplashActivity

class PerMissionActivity : BaseActivity() {
    private lateinit var mBinding : ActivityPerMissionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_per_mission)
        mBinding.activity = this
        mBinding.lifecycleOwner = this
        inStatusBar()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.gray4)
    }

    fun onConfirmClick(view: View) {
        if (!PermissionManager.getAllPermissionGranted(this)) {
            PermissionManager.requestAllPermissions(this)
        } else {
            prefs.setBool(Constant.PREF_PERMISSION_GRANTED, true)
            moveActivity(b = true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.ALL_PERMISSION_REQUEST_CODE) {
            if (PermissionManager.getAllPermissionGranted(this)) {
                prefs.setBool(Constant.PREF_PERMISSION_GRANTED, true)
                moveActivity(b = true)
            }
            else {
                if (getPermissionAllGranted()) {
                    Toast.makeText(this,"권한동의를 하지 않으면 앱을 이용하실 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
                else {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                }
            }
        }
    }

    private fun getPermissionAllGranted(): Boolean {
        var permissonNotShowDenied = true
        for (permission in Constant.MUTILE_PERMISSION) {
            if (!permission?.let {
                    shouldShowRequestPermissionRationale(it)
                }!!) {
                permissonNotShowDenied = false
            }
        }
        return permissonNotShowDenied
    }

    private fun moveActivity(b: Boolean) {
        val intent = Intent(this, SplashActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }
}