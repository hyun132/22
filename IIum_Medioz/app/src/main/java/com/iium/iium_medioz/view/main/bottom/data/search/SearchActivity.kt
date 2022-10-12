package com.iium.iium_medioz.view.main.bottom.data.search

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
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
import com.iium.iium_medioz.util.adapter.TestAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import com.iium.iium_medioz.viewmodel.main.bottom.data.search.SearchDataViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        viewModel.viewEvent.observe(this){
            it.getContentIfNotHandled()?.let { event ->
                when(event){
                    SearchDataViewModel.SHOW_ERROR_DIALOG -> ErrorDialog()
                }
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
            viewModel.myDataList.observe(this) {
                setAdapter(it)
            }
        }
    }

    private fun setAdapter(it: List<DataList>?) {
        val adapter = it?.let { it1 -> SearchAdapter(it1, this) }
        val rv = mBinding.searchRecyclerView
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)
        rv.setHasFixedSize(true)
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