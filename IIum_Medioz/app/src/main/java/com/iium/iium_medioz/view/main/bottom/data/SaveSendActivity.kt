package com.iium.iium_medioz.view.main.bottom.data

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySaveBinding
import com.iium.iium_medioz.databinding.ActivitySaveSendBinding
import com.iium.iium_medioz.util.base.BaseActivity

class SaveSendActivity : BaseActivity() {
    private lateinit var mBinding : ActivitySaveSendBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_save_send)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.colorPrimary)
    }

    fun onSaveClick(v: View) {
        moveMain()
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }
}