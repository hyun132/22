package com.iium.iium_medioz.view.main.bottom.data.send

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySaveBinding
import com.iium.iium_medioz.databinding.ActivitySendBinding
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.SEND_ID
import com.iium.iium_medioz.util.`object`.Constant.SEND_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.SEND_NORMAL
import com.iium.iium_medioz.util.`object`.Constant.SEND_TEXTIMG
import com.iium.iium_medioz.util.`object`.Constant.SEND_TIME_STAMP
import com.iium.iium_medioz.util.`object`.Constant.SEND_TITLE
import com.iium.iium_medioz.util.`object`.Constant.SEND_VIDEO
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.base.BaseActivity

class SendActivity : BaseActivity() {

    private lateinit var mBinding : ActivitySendBinding
    private lateinit var apiServices: APIService
    private var doubleBackToExit = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_send)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
        initView()
    }

    private fun initView() {
        val title = intent.getStringExtra(SEND_TITLE)
        val keyword = intent.getStringExtra(SEND_KEYWORD)
        val timestamp = intent.getStringExtra(SEND_TIME_STAMP)
        val textList = intent.getStringExtra(SEND_TEXTIMG)
        val normalList = intent.getStringExtra(SEND_NORMAL)
        val videoList = intent.getStringExtra(SEND_VIDEO)
        val id = intent.getStringExtra(SEND_ID)

        mBinding.tvMedicalDetailTitle.text = title.toString()
        mBinding.tvMedicalDetailData.text = timestamp.toString()
        mBinding.tvMyKeyword.text = keyword.toString()

    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.colorPrimary)
    }

    fun onBackPressed(v: View?) {
        moveDetail()
    }

    fun onSendClick(v: View?) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (doubleBackToExit) {
            finishAffinity()
        } else {
            doubleBackToExit = true
            runDelayed(1500L) {
                doubleBackToExit = false
            }
        }
    }

    private fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }
}