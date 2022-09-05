package com.iium.iium_medioz.view.main.bottom.insurance.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.api.NaverService
import com.iium.iium_medioz.databinding.FragmentSearchFirstBinding
import com.iium.iium_medioz.model.map.KaKaoDocuments
import com.iium.iium_medioz.model.map.KaKaoLocalModel
import com.iium.iium_medioz.model.map.KaKaoModel
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.adapter.map.KaKaoAdapter
import com.iium.iium_medioz.util.adapter.map.KaKaoLocalAdapter
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.extensions.onTextChanged
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.viewmodel.map.MapSearchViewModel
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import kotlinx.android.synthetic.main.fragment_search_first.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SearchFirstFragment : Fragment() {

    private lateinit var mBinding : FragmentSearchFirstBinding
    private var adapter: KaKaoLocalAdapter? = null
    private val Authorization = "KakaoAK 82794c09f76246fe3780f1e6e2be4bfd"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_first, container, false)
        mBinding.fragment = this
        initView()
        return mBinding.root
    }



    private fun initView() {
        val interceptor = HttpLoggingInterceptor()
        val gson = GsonBuilder().setLenient().create()
        val client = OkHttpClient.Builder()
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS)
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.KAKAO_API_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        val api = retrofit.create(NaverService::class.java)
        val query = mBinding.etSearch.text.toString()
        val callGetSearchNews = api.getKakaoLocal(Authorization, query, 45,15,"similar")

        LLog.e("카카오 로컬")
        callGetSearchNews.enqueue(object : Callback<KaKaoLocalModel> {
            override fun onResponse(
                call: Call<KaKaoLocalModel>,
                response: Response<KaKaoLocalModel>
            ) {
                val result = response.body()
                if(response.isSuccessful && result != null) {
                    Log.d(Constant.TAG, "카카오 로컬 성공 : $result")
                    try {
                        if(mBinding.rvResult.visibility == View.GONE) {
                            mBinding.rvResult.visibility = View.VISIBLE
                        }
                        if(mBinding.tvNoData.visibility == View.VISIBLE) {
                            mBinding.tvNoData.visibility = View.GONE
                        }
                        setAdapter(result.documents)
                    }catch (e: Exception) {
                        LLog.e(e.toString())
                    }
                } else {
                    Log.d(Constant.TAG,"카카오 로컬 성공 후 실패 ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<KaKaoLocalModel>, t: Throwable) {
                Log.d(Constant.TAG, "카카오 로컬 실패 : $t")
            }
        })
    }

    private fun setAdapter(documents: List<KaKaoDocuments>) {
        val mAdapter = KaKaoLocalAdapter(documents, context!!)
        mBinding.rvResult.adapter = mAdapter
        mBinding.rvResult.layoutManager = LinearLayoutManager(context!!)
        mBinding.rvResult.setHasFixedSize(true)
        return
    }


    override fun onResume() {
        super.onResume()
        adapter?.notifyDataSetChanged()
    }

}