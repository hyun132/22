package com.iium.iium_medioz.view.main.bottom.mypage.notice

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityNoticeBinding
import com.iium.iium_medioz.databinding.ActivitySettingBinding
import com.iium.iium_medioz.model.ui.Announcement
import com.iium.iium_medioz.model.ui.NoticeModel
import com.iium.iium_medioz.util.adapter.notice.NoticeAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeActivity : BaseActivity() {

    private lateinit var mBinding: ActivityNoticeBinding
    private lateinit var apiServices: APIService
    private var noticeAdapter : NoticeAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_notice)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
        initAPI()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initAPI() {
        LLog.e("공지사항_첫번째 API")
        val vercall: Call<NoticeModel> = apiServices.getNoti(prefs.myaccesstoken)
        vercall.enqueue(object : Callback<NoticeModel> {
            override fun onResponse(call: Call<NoticeModel>, response: Response<NoticeModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"NoticeModel response SUCCESS -> $result")
                    setAdapter(result.announcement!!)
                }
                else {
                    Log.d(LLog.TAG,"NoticeModel response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<NoticeModel>, t: Throwable) {
                Log.d(LLog.TAG, "NoticeModel Fail -> $t")
            }
        })
    }

    private fun otherAPI() {
        LLog.e("공지사항_두번째 API")
        val vercall: Call<NoticeModel> = apiServices.getNoti(prefs.newaccesstoken)
        vercall.enqueue(object : Callback<NoticeModel> {
            override fun onResponse(call: Call<NoticeModel>, response: Response<NoticeModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"NoticeModel Second response SUCCESS -> $result")
                    setAdapter(result.announcement!!)
                }
                else {
                    Log.d(LLog.TAG,"NoticeModel Second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<NoticeModel>, t: Throwable) {
                Log.d(LLog.TAG, "NoticeModel Second Fail -> $t")
            }
        })
    }

    private fun setAdapter(result: List<Announcement>) {
        val adapter = NoticeAdapter(result,this)
        val rv = findViewById<View>(R.id.notice_recyclerView) as RecyclerView
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
//        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    fun onBackPressed(v: View) {
        moveMain()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveMain()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        noticeAdapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }
}