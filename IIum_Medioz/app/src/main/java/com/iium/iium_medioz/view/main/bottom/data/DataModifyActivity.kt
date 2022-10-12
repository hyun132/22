package com.iium.iium_medioz.view.main.bottom.data

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityDataModifyBinding
import com.iium.iium_medioz.databinding.ActivitySaveBinding
import com.iium.iium_medioz.model.upload.DeleteModel
import com.iium.iium_medioz.model.upload.ModifyList
import com.iium.iium_medioz.model.upload.ModifyModel
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_ID
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_NORMAL
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_TEXTIMG
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_TITLE
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_VIDEO
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.viewmodel.main.bottom.DataModifyViewModel
import okhttp3.ResponseBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataModifyActivity : BaseActivity() {

    private lateinit var mBinding: ActivityDataModifyBinding
    private lateinit var apiServices: APIService
    private val viewModel: DataModifyViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_modify)
        apiServices = ApiUtils.apiService
        mBinding.apply {
            activity = this@DataModifyActivity
            lifecycleOwner = this@DataModifyActivity
            viewModel = this@DataModifyActivity.viewModel
        }
        inStatusBar()
        runOnUiThread {
            initView2()
        }


        viewModel.viewEvent.observe(this){
            it.getContentIfNotHandled()?.let { event ->
                when(event){
                    DataModifyViewModel.NAVIGATE_MAIN_ACTIVITY -> moveMain()
                }
            }
        }
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.colorPrimary)
    }

    private fun initView2() {
        val title = intent?.getStringExtra(DATA_MODIFY_TITLE)
        val keyword = intent?.getStringExtra(DATA_MODIFY_KEYWORD)
        val textList = intent?.getStringExtra(DATA_MODIFY_TEXTIMG)
        val normalList = intent?.getStringExtra(DATA_MODIFY_NORMAL)
        val videoList = intent?.getStringExtra(DATA_MODIFY_VIDEO)
        val id = intent.getStringExtra(DATA_MODIFY_ID)

        Log.d(LLog.TAG, "텍스트 이미지 : ${textList.toString()}")
        Log.d(LLog.TAG, "일반 이미지 : ${normalList.toString()}")
        Log.d(LLog.TAG, "비디오 이미지 : ${videoList.toString()}")

        viewModel.setViewData(title, keyword, textList, normalList, videoList,id)
    }

    fun onPutClick(v: View) {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(
            this,
            android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
        )
        dlg.setTitle("데이터 수정") //제목
        dlg.setMessage("데이터를 수정 하시겠습니까?") // 메시지
        dlg.setPositiveButton("확인") { dialog, which ->
            viewModel.initModify()
            dialog.dismiss()
        }
        dlg.setNegativeButton("취소") { dialog, which ->
            dialog.dismiss()
        }
        dlg.show()
    }
}