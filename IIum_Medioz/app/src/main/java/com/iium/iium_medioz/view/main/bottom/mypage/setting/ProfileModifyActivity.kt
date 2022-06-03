package com.iium.iium_medioz.view.main.bottom.mypage.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityProfileModifyBinding
import com.iium.iium_medioz.databinding.ActivitySettingBinding
import com.iium.iium_medioz.util.base.BaseActivity

class ProfileModifyActivity : BaseActivity() {

    private lateinit var mBinding : ActivityProfileModifyBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_modify)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    fun onBackPressed(v: View) {
        moveSetting()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveMain()
    }
}