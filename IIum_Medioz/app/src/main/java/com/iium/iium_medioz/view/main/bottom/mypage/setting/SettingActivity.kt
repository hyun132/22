package com.iium.iium_medioz.view.main.bottom.mypage.setting

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.BuildConfig
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySettingBinding
import com.iium.iium_medioz.util.base.BaseActivity


class SettingActivity : BaseActivity() {

    private lateinit var mBinding : ActivitySettingBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_setting)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        runOnUiThread {
            initView()
        }
        inStatusBar()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val versionNames = BuildConfig.VERSION_NAME
        mBinding.tvAppVersion.text = "v $versionNames"
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    fun onBackPressed(v: View) {
        moveMain()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveMain()
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }
}