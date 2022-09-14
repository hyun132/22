package com.iium.iium_medioz.view.main.bottom.insurance.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.GsonBuilder
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.api.NaverService
import com.iium.iium_medioz.databinding.ActivitySearchAddressBinding
import com.iium.iium_medioz.model.map.Documents
import com.iium.iium_medioz.model.map.Items
import com.iium.iium_medioz.model.map.KaKaoModel
import com.iium.iium_medioz.model.map.NaverSearchModel
import com.iium.iium_medioz.util.`object`.Constant.KAKAO_API_URL
import com.iium.iium_medioz.util.`object`.Constant.NAVER_API_URL
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.adapter.ViewPagerAdapter
import com.iium.iium_medioz.util.adapter.address.SearchAddressAdapter
import com.iium.iium_medioz.util.adapter.map.KaKaoAdapter
import com.iium.iium_medioz.util.adapter.map.SearchFragmentAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.log.LLog
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.IllegalArgumentException
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class SearchAddressActivity : BaseActivity() {
    private val tabTitleArray = arrayOf(
        "지역검색",
        "병원검색",
    )
    private lateinit var mBinding : ActivitySearchAddressBinding
    private lateinit var apiService: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_address)
        mBinding.activity = this
        apiService = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        initView()
        inStatusBar()

    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView() {
        try {
            super.onDetachedFromWindow()
            val viewPager = mBinding.vpSearch
            val tabLayout = mBinding.tlSearch

            viewPager.adapter = SearchFragmentAdapter(supportFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitleArray[position]
            }.attach()
        } catch (e: IllegalArgumentException) {
           Log.d(TAG,e.toString())
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveHospital()
    }
}