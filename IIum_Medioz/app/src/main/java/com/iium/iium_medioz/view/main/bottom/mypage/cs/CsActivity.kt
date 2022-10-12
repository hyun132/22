package com.iium.iium_medioz.view.main.bottom.mypage.cs

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.iium.iium_medioz.model.ui.CounList
import com.iium.iium_medioz.util.adapter.cs.CounSelingAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.viewmodel.main.bottom.data.mypage.cs.CsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CsActivity : BaseActivity() {
    private lateinit var mBinding: ActivityCsBinding
    private lateinit var apiServices: APIService
    private var counAdapter: CounSelingAdapter? = null
    private val viewModel: CsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cs)
        apiServices = ApiUtils.apiService
        mBinding.apply {
            lifecycleOwner = this@CsActivity
            activity = this@CsActivity
            viewModel = this@CsActivity.viewModel
        }
        inStatusBar()
        viewModel.counList.observe(this){
            setAdapter(it)
        }
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status)
    }

    private fun setAdapter(userRequest: List<CounList>) {
        val adapter = CounSelingAdapter(userRequest, this)
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