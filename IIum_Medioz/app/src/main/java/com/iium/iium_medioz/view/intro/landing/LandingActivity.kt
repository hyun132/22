package com.iium.iium_medioz.view.intro.landing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityLandingBinding
import com.iium.iium_medioz.databinding.ActivitySplashBinding
import com.iium.iium_medioz.util.base.BaseActivity

class LandingActivity : BaseActivity() {

    private lateinit var mBinding: ActivityLandingBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_landing)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
    }
}