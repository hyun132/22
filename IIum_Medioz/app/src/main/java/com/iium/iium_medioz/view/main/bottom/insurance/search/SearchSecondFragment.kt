package com.iium.iium_medioz.view.main.bottom.insurance.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.api.NaverService
import com.iium.iium_medioz.databinding.FragmentDataBinding
import com.iium.iium_medioz.databinding.FragmentSearchSecondBinding
import com.iium.iium_medioz.model.map.Documents
import com.iium.iium_medioz.model.map.Items
import com.iium.iium_medioz.model.map.KaKaoModel
import com.iium.iium_medioz.util.`object`.Constant.KAKAO_API_URL
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.adapter.address.SearchAddressAdapter
import com.iium.iium_medioz.util.adapter.map.KaKaoAdapter
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.view.main.bottom.data.search.SearchActivity
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.HospitalActivity
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SearchSecondFragment : Fragment() {

    private lateinit var mBinding : FragmentSearchSecondBinding
    private lateinit var apiServices: APIService
    private val clientId = "BQwUhf6Ll2GKA8WIPgkj"
    private val clientSecret = "hIT8qlMzxS"
    private val Authorization = "KakaoAK 82794c09f76246fe3780f1e6e2be4bfd"
    private var kakapAdapter: KaKaoAdapter?=null
    private var searchAddapter: SearchAddressAdapter?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_second, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        initView()
        initMakeSpinner()
        initSpinner()
        return mBinding.root
    }

    private fun initView() {
        mBinding.textField.setOnEditorActionListener { textView, i, keyEvent ->
            return@setOnEditorActionListener when (i) {
                EditorInfo.IME_ACTION_DONE -> {
                    initAPI()
                    mBinding.rvSearch.layoutManager = LinearLayoutManager(context!!)
                    mBinding.rvSearch.setHasFixedSize(true)
                    true
                }
                else -> false
            }
        }
    }

    private fun initMakeSpinner() {
        val person = resources.getStringArray(R.array.spinner_address)
        val perAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, person)
        mBinding.spnCustom.adapter = perAdapter
    }

    private fun initSpinner() {
        mBinding.spnCustom.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                mBinding.spinnerText.text = mBinding.spnCustom.getItemAtPosition(position).toString()

            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d(TAG,"NotingSelected Spinner : ${p0.toString()}")
            }

        }
    }

        private fun initAPI() {
        LLog.e("제휴병원 검색")
        val title = mBinding.textField.text.toString()
        val interceptor = HttpLoggingInterceptor()
        val gson = GsonBuilder().setLenient().create()
        val client = OkHttpClient.Builder()
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(KAKAO_API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api = retrofit.create(NaverService::class.java)
        val spinner = mBinding.spinnerText.text
        val query = if(spinner.equals("선택")) {
            title
        } else {
            "$spinner $title"
        }
        val callGetSearchNews = api.getKaKaoSearch(Authorization, query, 45,15)

        callGetSearchNews.enqueue(object : Callback<KaKaoModel> {
            override fun onResponse(
                call: Call<KaKaoModel>,
                response: Response<KaKaoModel>
            ) {
                val result = response.body()
                if(response.isSuccessful && result != null) {
                    Log.d(TAG, "성공 : $result")
                    try {
                        if(mBinding.clSearchAddress.visibility == View.GONE) {
                            mBinding.clSearchAddress.visibility = View.VISIBLE
                        }
                        if(mBinding.tvNoData.visibility == View.VISIBLE) {
                            mBinding.tvNoData.visibility = View.GONE
                        }
                        setAdapter(result.documents)
                    }catch (e: Exception) {
                        LLog.e(e.toString())
                    }
                } else {
                    Log.d(TAG,"성공 후 실패 ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<KaKaoModel>, t: Throwable) {
                Log.d(TAG, "실패 : $t")
            }
        })
    }

        private fun setAdapter(documents: List<Documents>) {
        val mAdapter = KaKaoAdapter(documents, context!!)
        mBinding.rvSearch.adapter = mAdapter
        mBinding.rvSearch.layoutManager = LinearLayoutManager(context!!)
        mBinding.rvSearch.setHasFixedSize(true)
        return
    }

    override fun onResume() {
        super.onResume()
        searchAddapter?.notifyDataSetChanged()
        kakapAdapter?.notifyDataSetChanged()
    }

}