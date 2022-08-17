package com.iium.iium_medioz.util.popup

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityImageNoticePopupBinding
import com.iium.iium_medioz.util.`object`.Constant.IMAGE_TIMEOUT
import com.iium.iium_medioz.util.`object`.Constant.INTENT_NOTICE_END_DATE
import com.iium.iium_medioz.util.`object`.Constant.INTENT_NOTICE_URL
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.squareup.picasso.Callback
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class ImageNoticePopup : BaseActivity() {

    private lateinit var mBinding: ActivityImageNoticePopupBinding
    private var endDate: String? = null
    private var mTimeoutHandler: Handler? = null
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_notice_popup)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        initView()
    }

    private fun initView() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        try {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        } catch (e: IllegalStateException) {
            LLog.e("IllegalStateException occuor")
        }

        val intent = intent
        val url = intent.getStringExtra(INTENT_NOTICE_URL)
        endDate = intent.getStringExtra(INTENT_NOTICE_END_DATE)

        mBinding.root.setOnClickListener { v -> }

        mBinding.root.visibility = View.GONE
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.connectTimeout(10, TimeUnit.SECONDS)
        okHttpClient.readTimeout(10, TimeUnit.SECONDS)
        okHttpClient.writeTimeout(10, TimeUnit.SECONDS)
        val picasso =
            Picasso.Builder(this).downloader(OkHttp3Downloader(okHttpClient.build())).build()

        LLog.e("URL: $url")
        showProgress()
        picasso.load(url).into(mBinding.imageview, object : Callback {
            override fun onSuccess() {
                mTimeoutHandler?.removeMessages(0)
                stopProgress()
                mBinding.root.visibility = View.VISIBLE
            }

            override fun onError(e: Exception) {
                LLog.e(e.message)
                mTimeoutHandler?.removeMessages(0)
                stopProgress()
                finish()
            }
        })

        val looper = Looper.myLooper()
        mTimeoutHandler = Handler(looper!!)
        mTimeoutHandler!!.postDelayed(Runnable {
            mTimeoutHandler?.removeMessages(0)
            //progressTimeout();
            if (!instance!!.isFinishing) {
                stopProgress()
                picasso.shutdown()
                finish()
            }
        }, IMAGE_TIMEOUT.toLong())
    }

    fun onNotshowClick(v: View) {
        val check = mBinding.btnNotshow.isSelected
        mBinding.btnNotshow.isSelected = !check
    }

    fun onConfirmClick(v: View) {
        if (mBinding.btnNotshow.isSelected) {
            prefs.setMainPopupEndDate(endDate)
            setResult(RESULT_CANCELED)
        }
        overridePendingTransition(0, 0)
        finish()
    }
}