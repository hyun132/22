package com.iium.iium_medioz.view.main.bottom.data.search

import android.app.AlertDialog

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySearchDetailBinding
import com.iium.iium_medioz.model.upload.DeleteModel
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.viewmodel.main.bottom.data.search.SearchDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchDetailActivity : BaseActivity() {

    private lateinit var mBinding : ActivitySearchDetailBinding
    private lateinit var apiServices: APIService
    private val viewModel: SearchDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_search_detail)
        apiServices = ApiUtils.apiService
        mBinding.apply {
            activity = this@SearchDetailActivity
            lifecycleOwner = this@SearchDetailActivity
            viewModel = this@SearchDetailActivity.viewModel
        }
        inStatusBar()
//        initView()
        initView2()

        viewModel.viewEvent.observe(this){
            it.getContentIfNotHandled()?.let { event ->
                when(event){
                    SearchDetailViewModel.SHOW_ERROR_DIALOG -> ErrorDialog()
                    SearchDetailViewModel.NAVIGATE_SAVE_SEND_ACTIVITY -> moveMain()
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
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView2() {
        val title = intent?.getStringExtra(Constant.SEARCH_TITLE)
        val keyword = intent?.getStringExtra(Constant.SEARCH_KEYWORD)
        val timestamp = intent?.getStringExtra(Constant.SEARCH_TIME_STAMP)
        val textList = intent?.getStringExtra(Constant.SEARCH_TEXTIMG)
        val normalList = intent?.getStringExtra(Constant.SEARCH_NORMAL)
        val videoList = intent?.getStringExtra(Constant.SEARCH_VIDEO)

        Log.d(LLog.TAG, "텍스트 이미지 : ${textList.toString()}")
        Log.d(LLog.TAG, "일반 이미지 : ${normalList.toString()}")
        Log.d(LLog.TAG, "비디오 이미지 : ${videoList.toString()}")

        viewModel.setViewData(title, keyword, timestamp, textList, normalList, videoList)
    }


    fun onPutClick(v: View) {

    }

    fun onDeleteClick(v: View) {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle("판매 해제") //제목
        dlg.setMessage("판매를 해제하시겠습니까?") // 메시지
        dlg.setPositiveButton("확인") { dialog, which ->
            viewModel.initDelete(Constant.DATA_ID)
            dialog.dismiss()
        }
        dlg.setNegativeButton("취소") { dialog, which ->
            dialog.dismiss()
        }
        dlg.show()
    }

//    private fun initDelete() {
//        val id = intent.getStringExtra(Constant.DATA_ID)
//        LLog.e("데이터 삭제 API")
//        val vercall: Call<DeleteModel> = apiServices.getDataDelete(MyApplication.prefs.newaccesstoken,id)
//        vercall.enqueue(object : Callback<DeleteModel> {
//            override fun onResponse(call: Call<DeleteModel>, response: Response<DeleteModel>) {
//                val result = response.body()
//                if (response.isSuccessful && result != null) {
//                    Log.d(LLog.TAG,"데이터 삭제 response SUCCESS -> $result")
//                    moveMain()
//                }
//                else {
//                    Log.d(LLog.TAG,"데이터 삭제  response ERROR -> $id")
//                    ErrorDialog()
//                }
//            }
//            override fun onFailure(call: Call<DeleteModel>, t: Throwable) {
//                Log.d(LLog.TAG, "데이터 삭제 Fail -> ${t.localizedMessage}")
//                ErrorDialog()
//            }
//        })
//    }


    fun onBackPressed(v: View) {
        moveSearch()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveSearch()
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }
}