package com.iium.iium_medioz.view.main.bottom.data.tablayout

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentSendBinding
import com.iium.iium_medioz.model.send.SendList
import com.iium.iium_medioz.model.send.SendTestModel
import com.iium.iium_medioz.util.adapter.SendAdapter
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendFragment : Fragment() {

    private lateinit var mBinding : FragmentSendBinding
    private lateinit var apiServices: APIService
    private var sendadapter : SendAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_send, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        initView()
        return mBinding.root
    }

    private fun initView() {
        LLog.e("판매 데이터 조회 API")
        val vercall: Call<SendTestModel> = apiServices.getSend(prefs.newaccesstoken)
        vercall.enqueue(object : Callback<SendTestModel> {
            override fun onResponse(call: Call<SendTestModel>, response: Response<SendTestModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"판매 데이터 조회 API SUCCESS -> $result")
                    if(result.datalist!!.isEmpty()) {
                        mBinding.SendListRecyclerView.visibility = View.GONE
                        mBinding.tvDataSendNot.visibility = View.VISIBLE
                    } else {
                        mBinding.SendListRecyclerView.visibility = View.VISIBLE
                        mBinding.tvDataSendNot.visibility = View.GONE
                        setAdapter(result.datalist)
                    }
                }
                else {
                    Log.d(LLog.TAG,"판매 데이터 조회 API ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<SendTestModel>, t: Throwable) {
                Log.d(LLog.TAG, "판매 데이터 조회 API Fail -> $t")
            }
        })
    }


    @SuppressLint("UseRequireInsteadOfGet")
    private fun setAdapter(datalist: List<SendList>) {
        val adapter = SendAdapter(datalist, context!!)
        val rv = mBinding.SendListRecyclerView
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(context)
        rv.setHasFixedSize(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        sendadapter?.notifyDataSetChanged()
    }
}