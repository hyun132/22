package com.iium.iium_medioz.api

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var retrofit : Retrofit? = null
    private var okhttp: OkHttpClient? = null
    var gson =GsonBuilder().setLenient().create()

<<<<<<< HEAD
    init { //로그 설정
        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        okhttp = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

=======
>>>>>>> 82c5e9611e2b428117cc5836bbbbd1b60e8ba8fa
    fun getClient(baseUrl: String): Retrofit {

        if(retrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(OkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            okhttp = OkHttpClient.Builder()
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .addInterceptor(interceptor)
                .build()
        }
        return retrofit!!
    }
}