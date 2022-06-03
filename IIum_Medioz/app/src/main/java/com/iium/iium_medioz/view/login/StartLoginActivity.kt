package com.iium.iium_medioz.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityStartLoginBinding
import com.iium.iium_medioz.util.base.BaseActivity

class StartLoginActivity : BaseActivity() {
    private lateinit var mBinding : ActivityStartLoginBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_start_login)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        inStatusBar()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.colorPrimary)
    }

    fun onOkClick(v: View?) {
        moveLogins()
    }
}