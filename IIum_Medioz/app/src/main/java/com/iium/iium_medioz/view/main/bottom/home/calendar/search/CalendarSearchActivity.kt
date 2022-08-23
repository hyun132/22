package com.iium.iium_medioz.view.main.bottom.home.calendar.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.toObjects
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityCalendarSearchBinding
import com.iium.iium_medioz.databinding.ActivityMainBinding
import com.iium.iium_medioz.model.calendar.FeelModel
import com.iium.iium_medioz.model.calendar.Jurnal
import com.iium.iium_medioz.util.adapter.CalendarAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.feel.ResultState
import com.iium.iium_medioz.util.feel.dp
import com.iium.iium_medioz.util.feel.getDeviceId
import com.iium.iium_medioz.util.firebase.FirebaseService
import com.iium.iium_medioz.util.vertical.VerticalSpaceItemDecoration
import com.iium.iium_medioz.view.main.bottom.band.BandFragment
import com.iium.iium_medioz.view.main.bottom.data.DataFragment
import com.iium.iium_medioz.view.main.bottom.home.HomeFragment
import com.iium.iium_medioz.view.main.bottom.home.calendar.edit.CalendarEditActivity
import com.iium.iium_medioz.view.main.bottom.insurance.InsuranceFragment
import com.iium.iium_medioz.viewmodel.calendar.SearchViewModel

class CalendarSearchActivity : BaseActivity() {

    private lateinit var mBinding : ActivityCalendarSearchBinding
    private lateinit var apiServices: APIService
    private val viewModel by viewModels<SearchViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_calendar_search)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
        initView()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView() {
        mBinding.rvSearch.layoutManager = LinearLayoutManager(this)
        mBinding.rvSearch.addItemDecoration(VerticalSpaceItemDecoration(16.dp))

        mBinding.etSearch.addTextChangedListener {
            mBinding.btnClear.isVisible = it.toString().isNotEmpty()
        }

        mBinding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            }
            false
        }

        viewModel.resultStateDeleteJurnal.observe(this) {
            when (it) {
                is ResultState.Error -> {
                    Log.e("Delete Data", it.e.message.toString())

                }
                is ResultState.Success<*> -> {
                    Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
                is ResultState.Loading -> {

                }
            }
        }
    }

    fun onSearchClick(v: View) {
        performSearch()
    }

    fun onClearClick(v: View) {
        mBinding.rvSearch.isVisible = false
        mBinding.wording.isVisible = false
        mBinding.etSearch.text.clear()
    }

    fun onCancelClick(v: View) {
        finish()
    }

    private fun performSearch() {
        mBinding.pbSearch.isVisible = true
        mBinding.rvSearch.isVisible = true
        FirebaseService.getText(getDeviceId())
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    e.printStackTrace()
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val model = snapshot.toObjects<Jurnal>().toMutableList()
                    val match =
                        model.filter { fil ->
                            fil.title
                                .lowercase().contains(
                                    mBinding.etSearch.text.toString()
                                        .lowercase()
                                )
                        }

                    val adapter = CalendarAdapter(match as MutableList<Jurnal>)
                    adapter.setClickListener { jun ->
                        val jurnal = FeelModel(
                            jun.background,
                            jun.date,
                            jun.feeling,
                            jun.fileName,
                            jun.msg,
                            jun.title,
                            jun.jurnalId
                        )
                        Intent(this, CalendarEditActivity::class.java).apply {
                            putExtra("jurnal", jurnal)
                            startActivity(this)
                        }
                    }
                    mBinding.rvSearch.adapter = adapter
                    adapter.setClickDelete(viewModel::deleteJurnal)
                    mBinding.wording.isVisible = true
                } else {
                    val adapter = CalendarAdapter(mutableListOf())
                    adapter.isSearchPage = true
                    mBinding.rvSearch.adapter = adapter
                    mBinding.wording.isVisible = true
                }

                mBinding.pbSearch.isVisible = false
            }
        mBinding.etSearch.clearFocus()
        val input: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(mBinding.etSearch.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }

}