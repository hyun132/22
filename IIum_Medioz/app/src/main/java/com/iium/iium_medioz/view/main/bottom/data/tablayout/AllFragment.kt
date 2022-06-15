package com.iium.iium_medioz.view.main.bottom.data.tablayout

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentAllBinding
import com.iium.iium_medioz.model.recycler.*
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.adapter.TestAdapter
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllFragment : Fragment() {

    private lateinit var mBinding : FragmentAllBinding
    private lateinit var apiServices: APIService
    private var readapter: TestAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_all, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        initView()

        return mBinding.root
    }

    private fun initView() {
        LLog.e("데이터 조회_첫번째 API")
        val vercall: Call<MedicalData> = apiServices.getCreateGet(prefs.myaccesstoken)
        vercall.enqueue(object : Callback<MedicalData> {
            override fun onResponse(call: Call<MedicalData>, response: Response<MedicalData>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"List response SUCCESS -> $result")
                    setAdapter(result.datalist)
                }
                else {
                    Log.d(LLog.TAG,"데이터 조회_첫번째 API List response ERROR -> $result")
                    otherAPI()
                }
            }
            override fun onFailure(call: Call<MedicalData>, t: Throwable) {
                Log.d(LLog.TAG, "List Fail -> $t")
            }
        })
    }

    private fun otherAPI() {
        LLog.e("데이터 조회_두번째 API")
        val vercall: Call<MedicalData> = apiServices.getCreateGet(prefs.newaccesstoken)
        vercall.enqueue(object : Callback<MedicalData> {
            override fun onResponse(call: Call<MedicalData>, response: Response<MedicalData>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"List Second response SUCCESS -> $result")
                    setAdapter(result.datalist)
                }
                else {
                    Log.d(LLog.TAG,"List Second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<MedicalData>, t: Throwable) {
                Log.d(LLog.TAG, "List Second Fail -> $t")
            }
        })
    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun setAdapter(datalist: List<DataList>?) {
        val adapter = TestAdapter(datalist!!, context!!)
        val rv = mBinding.medicalRecyclerView
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
        rv.setHasFixedSize(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        readapter?.notifyDataSetChanged()
    }
}