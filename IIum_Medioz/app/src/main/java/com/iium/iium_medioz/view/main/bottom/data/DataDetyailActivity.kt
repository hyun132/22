package com.iium.iium_medioz.view.main.bottom.data

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityDataDetyailBinding
import com.iium.iium_medioz.databinding.ActivityMainBinding
import com.iium.iium_medioz.util.`object`.Constant.DATA_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.DATA_TIMESTAMP
import com.iium.iium_medioz.util.`object`.Constant.DATA_TITLE
import com.iium.iium_medioz.util.base.BaseActivity

class DataDetyailActivity : BaseActivity() {

    private lateinit var mBinding : ActivityDataDetyailBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_detyail)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        initView()
    }

    private fun initView() {
        val title = intent.getStringExtra(DATA_TITLE)
        val keyword = intent.getStringExtra(DATA_KEYWORD)
        val timestamp = intent.getStringExtra(DATA_TIMESTAMP)

        mBinding.tvMedicalDetailTitle.text = title.toString()
        mBinding.tvMedicalDetailData.text = timestamp.toString()
        mBinding.tvMyKeyword.text = keyword.toString()
    }

    fun onBackPressed(v: View?){
        moveMain()
    }

    fun onDataSend(v: View?) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveMain()
    }
}