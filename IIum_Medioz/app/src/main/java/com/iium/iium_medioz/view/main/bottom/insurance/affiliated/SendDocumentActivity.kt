package com.iium.iium_medioz.view.main.bottom.insurance.affiliated

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityDocumentBinding
import com.iium.iium_medioz.databinding.ActivitySendDocumentBinding
import com.iium.iium_medioz.util.base.BaseActivity

class SendDocumentActivity : BaseActivity() {
    private lateinit var mBinding : ActivitySendDocumentBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_send_document)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        runOnUiThread {
            initView()
            inStatusBar()
        }
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView() {

    }

    fun onCheckClick(v: View) {
        val inent = Intent(this, DocumentActivity::class.java)
        startActivity(inent)
    }
}