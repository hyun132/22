package com.iium.iium_medioz.view.main.bottom.band.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityBandSearchBinding
import com.iium.iium_medioz.databinding.ActivitySearchBinding
import com.iium.iium_medioz.util.adapter.SearchAdapter
import com.iium.iium_medioz.util.base.BaseActivity

class BandSearchActivity : BaseActivity() {
    private lateinit var mBinding : ActivityBandSearchBinding
    private lateinit var apiServices: APIService
    private var bandsearchAdapter : SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_band_search)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    fun onBackPressed(v: View) {

    }

    fun onSearchClick(v: View) {

    }
}