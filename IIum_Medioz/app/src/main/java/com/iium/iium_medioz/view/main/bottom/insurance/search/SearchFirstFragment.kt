package com.iium.iium_medioz.view.main.bottom.insurance.search

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentSearchFirstBinding
import com.iium.iium_medioz.model.map.AddressDocument
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.util.adapter.map.AddressListAdapter
import com.iium.iium_medioz.util.adapter.map.KaKaoAdapter
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFirstFragment : Fragment() {

    private lateinit var mBinding : FragmentSearchFirstBinding
    private lateinit var apiServices: APIService
    private var readapter: AddressListAdapter?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_first, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        initView()
        return mBinding.root
    }

    private fun initView() {
        mBinding.textField.setOnEditorActionListener { textView, i, keyEvent ->
            return@setOnEditorActionListener when (i) {
                EditorInfo.IME_ACTION_DONE -> {
                    initAPI()
                    mBinding.searchResult.layoutManager = LinearLayoutManager(context!!)
                    mBinding.searchResult.setHasFixedSize(true)
                    true
                }
                else -> false
            }
        }
    }

    private fun initAPI() {
        LLog.e("병원 데이터API")
        val query = mBinding.textField.text.toString()
        val vercall: Call<MapMarker> = apiServices.getMap(prefs.newaccesstoken,query)
        vercall.enqueue(object : Callback<MapMarker> {
            override fun onResponse(call: Call<MapMarker>, response: Response<MapMarker>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"병원 데이터API response SUCCESS -> $result")
                    setAdapter(result.result)
                }
                else {
                    Log.d(LLog.TAG,"병원 데이터API response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<MapMarker>, t: Throwable) {
                Log.d(LLog.TAG, "병원 데이터API Fail -> $t")
            }
        })
    }

    private fun setAdapter(result: List<AddressDocument>) {
        val mAdapter = AddressListAdapter(result, context!!)
        mBinding.searchResult.adapter = mAdapter
        mBinding.searchResult.layoutManager = LinearLayoutManager(context!!)
        mBinding.searchResult.setHasFixedSize(true)
        return
    }

    override fun onResume() {
        super.onResume()
        readapter?.notifyDataSetChanged()
    }
}

