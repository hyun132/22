package com.iium.iium_medioz.view.main.bottom.data.send

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySaveBinding
import com.iium.iium_medioz.databinding.ActivitySendDetailBinding
import com.iium.iium_medioz.model.upload.DeleteModel
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_ID
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_NORMAL
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_TEXTIMG
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_TIME_STAMP
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_TITLE
import com.iium.iium_medioz.util.`object`.Constant.SEND_DETAIL_VIDEO
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.viewmodel.main.bottom.data.send.SendDetailViewModel
import okhttp3.ResponseBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendDetailActivity : BaseActivity() {

    private lateinit var mBinding : ActivitySendDetailBinding
    private lateinit var apiServices: APIService
    private val viewModel: SendDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_send_detail)
        apiServices = ApiUtils.apiService
        mBinding.apply {
            activity = this@SendDetailActivity
            lifecycleOwner = this@SendDetailActivity
            viewModel = this@SendDetailActivity.viewModel
        }
        inStatusBar()
        runOnUiThread {
            initView2()
        }

        viewModel.viewEvent.observe(this){
            it.getContentIfNotHandled()?.let { event ->
                when(event){
                    SendDetailViewModel.NAVIGATE_MAIN_ACTIVITY -> moveMain()
                    SendDetailViewModel.SHOW_ERROR_DIALOG -> ErrorDialog()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.colorPrimary)
    }

    private fun initView2() {
        val title = intent.getStringExtra(SEND_DETAIL_TITLE)
        val keyword = intent.getStringExtra(SEND_DETAIL_KEYWORD)
        val timestamp = intent.getStringExtra(SEND_DETAIL_TIME_STAMP)
        val textList = intent.getStringExtra(SEND_DETAIL_TEXTIMG)
        val normalList = intent.getStringExtra(SEND_DETAIL_NORMAL)
        val videoList = intent.getStringExtra(SEND_DETAIL_VIDEO)
        val id = intent.getStringExtra(SEND_DETAIL_ID)

        viewModel.setViewData(title,keyword,timestamp,textList,normalList,videoList,id)
    }

    fun onDeleteClick(v: View) {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle("판매 해제") //제목
        dlg.setMessage("판매를 해제하시겠습니까?") // 메시지
        dlg.setPositiveButton("확인") { dialog, which ->
//            initDelete()
            viewModel.initDelete()
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