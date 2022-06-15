package com.iium.iium_medioz.view.main.bottom.data.search

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
import com.iium.iium_medioz.databinding.ActivityMainBinding
import com.iium.iium_medioz.databinding.ActivitySearchBinding
import com.iium.iium_medioz.model.rest.base.CreateName
import com.iium.iium_medioz.model.rest.base.DataList
import com.iium.iium_medioz.util.adapter.SearchAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : BaseActivity() {

    private lateinit var mBinding : ActivitySearchBinding
    private lateinit var apiServices: APIService
    private var searchAdapter : SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_search)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initAPI() {
        LLog.e("검색_첫번째 API")
        val name = mBinding.etSearch.text.toString()
        val vercall: Call<CreateName> = apiServices.getSearch(name,prefs.myaccesstoken)
        vercall.enqueue(object : Callback<CreateName> {
            override fun onResponse(call: Call<CreateName>, response: Response<CreateName>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"Search response SUCCESS -> $result")
                    setAdapter(result.result)
                }
                else {
                    Log.d(TAG,"Search response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<CreateName>, t: Throwable) {
                Log.d(TAG, "Search Fail -> $t")
            }
        })
    }

    private fun otherAPI() {
        LLog.e("검색_두번째 API")
        val name = mBinding.etSearch.text.toString()
        val vercall: Call<CreateName> = apiServices.getSearch(name,prefs.newaccesstoken)
        vercall.enqueue(object : Callback<CreateName> {
            override fun onResponse(call: Call<CreateName>, response: Response<CreateName>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"Search Second response SUCCESS -> $result")
                    setAdapter(result.result)
                }
                else {
                    Log.d(TAG,"Search Second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<CreateName>, t: Throwable) {
                Log.d(TAG, "Search Second Fail -> $t")
            }
        })
    }

    private fun setAdapter(datalist: List<DataList>?) {
        val mAdapter = datalist?.let {
            SearchAdapter(it,this)
        }
        mBinding.searchRecyclerView.adapter = mAdapter
        mBinding.searchRecyclerView.layoutManager = LinearLayoutManager(this)
        mBinding.searchRecyclerView.setHasFixedSize(true)
        return
    }

    fun onSearchClick(v: View) {
        val search = mBinding.etSearch.text.toString()
        if (search.isEmpty()) {
            mBinding.etSearch.error = "미입력"
        }
        else {
            initAPI()
        }
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
        searchAdapter?.notifyDataSetChanged()
    }
}