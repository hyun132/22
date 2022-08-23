package com.iium.iium_medioz.view.main.bottom.mypage.cs

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
import com.iium.iium_medioz.databinding.ActivityCsBinding
import com.iium.iium_medioz.databinding.ActivityNoticeDetailBinding
import com.iium.iium_medioz.model.ui.CounGet
import com.iium.iium_medioz.model.ui.CounList
import com.iium.iium_medioz.util.adapter.cs.CounSelingAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CsActivity : BaseActivity() {
    private lateinit var mBinding: ActivityCsBinding
    private lateinit var apiServices: APIService
    private var counAdapter : CounSelingAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cs)
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
        LLog.e("1:1문의_첫번째 API")
        val vercall: Call<CounGet> = apiServices.getCounRequest(prefs.myaccesstoken)
        vercall.enqueue(object : Callback<CounGet> {
            override fun onResponse(call: Call<CounGet>, response: Response<CounGet>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"CounGet response SUCCESS -> $result")
                    setAdapter(result.userRequest!!)
                }
                else {
                    Log.d(LLog.TAG,"CounGet response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<CounGet>, t: Throwable) {
                Log.d(LLog.TAG, "CounGet Fail -> $t")
            }
        })
    }

    private fun otherAPI() {
        LLog.e("1:1문의_두번째 API")
        val vercall: Call<CounGet> = apiServices.getCounRequest(prefs.newaccesstoken)
        vercall.enqueue(object : Callback<CounGet> {
            override fun onResponse(call: Call<CounGet>, response: Response<CounGet>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"CounGet Second response SUCCESS -> $result")
                    setAdapter(result.userRequest!!)
                }
                else {
                    Log.d(LLog.TAG,"CounGet Second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<CounGet>, t: Throwable) {
                Log.d(LLog.TAG, "CounGet Second Fail -> $t")
            }
        })
    }

    private fun setAdapter(userRequest: List<CounList>) {
        val adapter = CounSelingAdapter(userRequest,this)
        val rv = findViewById<View>(R.id.counSeling_recyclerView) as RecyclerView
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
        rv.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    fun onBackPressed(v: View) {
        moveMain()
    }

    fun onCounClick(v: View) {
        moveUploadCs()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveMain()
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        counAdapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }
}