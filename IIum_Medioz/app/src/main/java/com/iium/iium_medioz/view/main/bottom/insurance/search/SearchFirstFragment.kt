package com.iium.iium_medioz.view.main.bottom.insurance.search

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentSearchFirstBinding
import com.iium.iium_medioz.model.map.AddressDocument
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.util.adapter.map.AddressListAdapter
import com.iium.iium_medioz.util.adapter.map.HospitalAdapter
import com.iium.iium_medioz.util.adapter.map.KaKaoAdapter
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.extensions.onTextChanged
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.viewmodel.map.SearchViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFirstFragment : Fragment() {

    private lateinit var mBinding : FragmentSearchFirstBinding
    private lateinit var apiServices: APIService
    private var readapter: HospitalAdapter?=null
    private val viewModel: SearchViewModel by viewModels()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_first, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        initView()
        init()
        return mBinding.root
    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun initView() {
        viewModel.searchResult.observe(this, Observer {
        })
        viewModel.status.observe(this, Observer {
            if (it) {
                if (viewModel.kakaoList.value!!.result.isEmpty()) {
                    mBinding.searchResult.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                } else {
                    readapter?.submitList(viewModel.kakaoList.value!!.result)
                    if (mBinding.searchResult.visibility == View.GONE) {
                        mBinding.searchResult.visibility = View.VISIBLE
                    }
                    if (mBinding.tvNoData.visibility == View.VISIBLE) {
                        mBinding.tvNoData.visibility = View.GONE
                    }
                }
            } else {
                if (mBinding.searchResult.visibility == View.VISIBLE) {
                    mBinding.searchResult.visibility = View.GONE
                }
                if (mBinding.tvNoData.visibility == View.GONE) {
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun init() {
        readapter = HospitalAdapter()
        // RecyclerView 설정
        mBinding.searchResult.adapter = readapter
        mBinding.searchResult.setHasFixedSize(true)
        mBinding.searchResult.layoutManager = LinearLayoutManager(context)
//        readapter.setOnItemClickListener { x, y ->
//            val intent = Intent(this, MapActivity::class.java)
//            intent.putExtra("x", x)
//            intent.putExtra("y", y)
//            startActivity(intent)
//        }

        // EditText 입력 값에 변화가 있으면 BroadcastChannel로 값 전송
        mBinding.textField.onTextChanged { s, start, before, count ->
            val queryText = s.toString()
            readapter!!.submitVariable(queryText, latitude, longitude)
            // Channel에 queryText 전송, Channel 용량을 침범하지 않았다면 true 아니면 false 리턴
            viewModel.queryChannel.trySend(queryText).isSuccess
        }
    }
}

