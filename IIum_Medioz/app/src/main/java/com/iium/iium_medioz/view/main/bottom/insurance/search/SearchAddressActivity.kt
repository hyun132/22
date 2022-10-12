package com.iium.iium_medioz.view.main.bottom.insurance.search

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySearchAddressBinding
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.adapter.map.SearchFragmentAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import java.lang.IllegalArgumentException

class SearchAddressActivity : BaseActivity() {
    private val tabTitleArray = arrayOf(
        "지역명으로",
        "병원명으로",
    )
    private lateinit var mBinding : ActivitySearchAddressBinding
    private lateinit var apiService: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_address)
        mBinding.activity = this
        apiService = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        runOnUiThread {
            initView()
        }
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