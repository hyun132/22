package com.iium.iium_medioz.view.main.bottom.mypage.cs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityCsBinding
import com.iium.iium_medioz.databinding.ActivityCsDetailBinding
import com.iium.iium_medioz.util.base.BaseActivity

class CsDetailActivity : BaseActivity() {

    private lateinit var mBinding: ActivityCsDetailBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cs_detail)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }
}