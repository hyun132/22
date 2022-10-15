package com.iium.iium_medioz.view.main.bottom.data.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySearchBinding
import com.iium.iium_medioz.model.rest.base.DataList
import com.iium.iium_medioz.util.adapter.SearchAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.viewmodel.main.bottom.data.search.SearchDataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity() {

    private lateinit var mBinding: ActivitySearchBinding
    private lateinit var apiServices: APIService
    private var searchAdapter: SearchAdapter? = null
    private val viewModel: SearchDataViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        apiServices = ApiUtils.apiService
        mBinding.apply {
            activity = this@SearchActivity
            lifecycleOwner = this@SearchActivity
            viewModel = this@SearchActivity.viewModel
        }

        inStatusBar()

        viewModel.viewEvent.observe(this) {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    SearchDataViewModel.SHOW_ERROR_DIALOG -> ErrorDialog()
                }
            }
        }
        viewModel.myDataList.observe(this) {
            if(!it.isNullOrEmpty()) {
                setAdapter(it)
            }

        }
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status)
    }


    fun onSearchClick2(v: View) {
        val search = mBinding.etSearch.text.toString()
        if (search.isEmpty()) {
            mBinding.etSearch.error = "미입력"
        } else {
            viewModel.initAPI()
        }
    }

    private fun setAdapter(it: List<DataList>) {
        LLog.d("setAdapter")
        searchAdapter = SearchAdapter(it, this)
        mBinding.searchRecyclerView.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(this@SearchActivity)
            setHasFixedSize(true)
        }
        searchAdapter!!.notifyDataSetChanged()
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

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }
}