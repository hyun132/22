package com.iium.iium_medioz.view.main.bottom.data

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.exoplayer2.SimpleExoPlayer
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityDataDetyailBinding
import com.iium.iium_medioz.model.upload.DeleteModel
import com.iium.iium_medioz.util.`object`.Constant.DATA_ID
import com.iium.iium_medioz.util.`object`.Constant.DATA_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_ID
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_NORMAL
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_TEXTIMG
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_TITLE
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_VIDEO
import com.iium.iium_medioz.util.`object`.Constant.DATA_NORMAL
import com.iium.iium_medioz.util.`object`.Constant.DATA_TEXTIMG
import com.iium.iium_medioz.util.`object`.Constant.DATA_TIMESTAMP
import com.iium.iium_medioz.util.`object`.Constant.DATA_TITLE
import com.iium.iium_medioz.util.`object`.Constant.DATA_VIDEOFILE
import com.iium.iium_medioz.util.`object`.Constant.SEND_ID
import com.iium.iium_medioz.util.`object`.Constant.SEND_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.SEND_NORMAL
import com.iium.iium_medioz.util.`object`.Constant.SEND_TEXTIMG
import com.iium.iium_medioz.util.`object`.Constant.SEND_TIME_STAMP
import com.iium.iium_medioz.util.`object`.Constant.SEND_TITLE
import com.iium.iium_medioz.util.`object`.Constant.SEND_VIDEO
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import com.iium.iium_medioz.view.main.bottom.data.send.SendActivity
import com.iium.iium_medioz.viewmodel.main.bottom.DetailViewModel
import okhttp3.ResponseBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.system.exitProcess

class DataDetyailActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDataDetyailBinding
    private lateinit var apiServices: APIService
    private val viewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_detyail)
        apiServices = ApiUtils.apiService
        mBinding.apply {
            activity = this@DataDetyailActivity
            lifecycleOwner = this@DataDetyailActivity
            viewModel = this@DataDetyailActivity.viewModel
        }

        runOnUiThread {
            initView2()
//            initView()
        }
        inStatusBar()

        viewModel.mutableErrorMessage.observe(this) {
            Toast.makeText(this@DataDetyailActivity, it, Toast.LENGTH_SHORT)
        }

        viewModel.viewEvent.observe(this) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    DetailViewModel.NAVIGATE_MAIN_ACTIVITY -> moveMain()
                    DetailViewModel.SHOW_ERROR_DIALOG -> ErrorDialog()

                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        return
    }

    private fun initView2() {
        val title = intent?.getStringExtra(DATA_TITLE)
        val keyword = intent?.getStringExtra(DATA_KEYWORD)
        val timestamp = intent?.getStringExtra(DATA_TIMESTAMP)
        val textList = intent?.getStringExtra(DATA_TEXTIMG)
        val normalList = intent?.getStringExtra(DATA_NORMAL)
        val videoList = intent?.getStringExtra(DATA_VIDEOFILE)

        Log.d(TAG, "텍스트 이미지 : ${textList.toString()}")
        Log.d(TAG, "일반 이미지 : ${normalList.toString()}")
        Log.d(TAG, "비디오 이미지 : ${videoList.toString()}")

        viewModel.setViewData(title, keyword, timestamp, textList, normalList, videoList)
    }


    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status)

    }

    fun onBackPressed(v: View?) {
        moveMain()
    }

    fun onDataSend(v: View?) {
        val title = mBinding.tvMedicalDetailTitle.text.toString()
        val keyword = mBinding.tvMyKeyword.text.toString()
        val timestamp = mBinding.tvMedicalDetailData.text.toString()
        val textList = intent?.getStringExtra(DATA_TEXTIMG)
        val normalList = intent?.getStringExtra(DATA_NORMAL)
        val videoList = intent?.getStringExtra(DATA_VIDEOFILE)
        val id = intent?.getStringExtra(DATA_ID)

        val intent = Intent(this, SendActivity::class.java)
        intent.putExtra(SEND_TITLE, title)
        intent.putExtra(SEND_KEYWORD, keyword)
        intent.putExtra(SEND_TIME_STAMP, timestamp)
        intent.putExtra(SEND_TEXTIMG, textList.toString())
        intent.putExtra(SEND_NORMAL, normalList.toString())
        intent.putExtra(SEND_VIDEO, videoList.toString())
        intent.putExtra(SEND_ID, id.toString())
        startActivity(intent)
    }

    fun onPutClick2(v: View?) {
        val title = mBinding.tvMedicalDetailTitle.text.toString()
        val keyword = mBinding.tvMyKeyword.text.toString()
        val textList = intent?.getStringExtra(DATA_TEXTIMG)
        val normalList = intent?.getStringExtra(DATA_NORMAL)
        val videoList = intent?.getStringExtra(DATA_VIDEOFILE)
        val id = intent?.getStringExtra(DATA_ID)

        val intent = Intent(this, DataModifyActivity::class.java)
        intent.putExtra(DATA_MODIFY_TITLE, title)
        intent.putExtra(DATA_MODIFY_KEYWORD, keyword)
        intent.putExtra(DATA_MODIFY_TEXTIMG, textList.toString())
        intent.putExtra(DATA_MODIFY_NORMAL, normalList.toString())
        intent.putExtra(DATA_MODIFY_VIDEO, videoList.toString())
        intent.putExtra(DATA_MODIFY_ID, id.toString())
        startActivity(intent)
    }


    fun onDeleteClick(v: View?) {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(
            this,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
        dlg.setTitle("데이터 삭제") //제목
        dlg.setMessage("데이터를 삭제하시겠습니까?") // 메시지
        dlg.setPositiveButton("확인") { dialog, which ->
            viewModel.initDelete(intent.getStringExtra(DATA_ID) ?: "")
            dialog.dismiss()
        }
        dlg.setNegativeButton("취소") { dialog, which ->
            dialog.dismiss()
        }
        dlg.show()
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