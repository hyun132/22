package com.iium.iium_medioz.view.main.bottom.insurance.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.api.NaverService
import com.iium.iium_medioz.databinding.ActivitySearchAddressBinding
import com.iium.iium_medioz.model.map.Documents
import com.iium.iium_medioz.model.map.Items
import com.iium.iium_medioz.model.map.KaKaoModel
import com.iium.iium_medioz.model.map.NaverSearchModel
import com.iium.iium_medioz.util.`object`.Constant.KAKAO_API_URL
import com.iium.iium_medioz.util.`object`.Constant.NAVER_API_URL
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.adapter.address.SearchAddressAdapter
import com.iium.iium_medioz.util.adapter.map.KaKaoAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.log.LLog
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.net.URLEncoder
import java.util.concurrent.TimeUnit

class SearchAddressActivity : BaseActivity() {
    private val clientId = "BQwUhf6Ll2GKA8WIPgkj"
    private val clientSecret = "hIT8qlMzxS"
    private val Authorization = "KakaoAK 82794c09f76246fe3780f1e6e2be4bfd"
    private lateinit var mBinding : ActivitySearchAddressBinding
    private lateinit var apiService: APIService
    private var searchAddapter: SearchAddressAdapter?=null
    private var kakapAdapter: KaKaoAdapter?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_address)
        mBinding.activity = this
        apiService = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        initView()
        inStatusBar()
        initMakeSpinner()
        initSpinner()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView() {
        mBinding.textField.setOnEditorActionListener { textView, i, keyEvent ->
            return@setOnEditorActionListener when (i) {
                EditorInfo.IME_ACTION_DONE -> {
                    initAPI()
                    mBinding.rvSearch.layoutManager = LinearLayoutManager(this)
                    mBinding.rvSearch.setHasFixedSize(true)
                    true
                }
                else -> false
            }
        }
    }

    private fun initMakeSpinner() {
        val person = resources.getStringArray(R.array.spinner_address)
        val perAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, person)
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

//        val api = retrofit.create(NaverService::class.java)
//        val callGetSearchNews = api.getNaverSearch(clientId, clientSecret, title, 5,1,"random")
//
//        callGetSearchNews.enqueue(object : Callback<NaverSearchModel> {
//            override fun onResponse(
//                call: Call<NaverSearchModel>,
//                response: Response<NaverSearchModel>
//            ) {
//                val result = response.body()
//                if(response.isSuccessful && result != null) {
//                    Log.d(TAG, "성공 : $result")
//                    runOnUiThread {
//                        try {
//                            if(mBinding.clSearchAddress.visibility == View.GONE) {
//                                mBinding.clSearchAddress.visibility = View.VISIBLE
//                            }
//                            if(mBinding.tvNoData.visibility == View.VISIBLE) {
//                                mBinding.tvNoData.visibility = View.GONE
//                            }
//                            setAdapter(result.items)
//                        }catch (e: Exception) {
//                            LLog.e(e.toString())
//                        }
//                    }
//                } else {
//                    Log.d(TAG,"성공 후 실패 ${response.errorBody()}")
//                }
//            }
//
//            override fun onFailure(call: Call<NaverSearchModel>, t: Throwable) {
//                Log.d(TAG, "실패 : $t")
//            }
//        })

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
                    runOnUiThread {
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
        val mAdapter = KaKaoAdapter(documents,this)
        mBinding.rvSearch.adapter = mAdapter
        mBinding.rvSearch.layoutManager = LinearLayoutManager(this)
        mBinding.rvSearch.setHasFixedSize(true)
        return
    }

//    private fun setAdapter(items: List<Items>?) {
//        val mAdapter = items?.let {
//            SearchAddressAdapter(it,this)
//        }
//        mBinding.rvSearch.adapter = mAdapter
//        mBinding.rvSearch.layoutManager = LinearLayoutManager(this)
//        mBinding.rvSearch.setHasFixedSize(true)
//        return
//    }

    fun onCloseClick(v: View) {
        moveHospital()
    }

    override fun onResume() {
        super.onResume()
        searchAddapter?.notifyDataSetChanged()
        kakapAdapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveHospital()
    }
}