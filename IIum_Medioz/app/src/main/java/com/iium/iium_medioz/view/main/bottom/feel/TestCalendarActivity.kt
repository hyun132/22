package com.iium.iium_medioz.view.main.bottom.feel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityCalendarBinding
import com.iium.iium_medioz.databinding.ActivityTestCalendarBinding
import com.iium.iium_medioz.util.base.BaseActivity

class TestCalendarActivity :  BaseActivity() {

    private lateinit var mBinding : ActivityTestCalendarBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_test_calendar)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        runOnUiThread {
            inStatusBar()
            initView()
        }
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView() {

    }
}