package com.iium.iium_medioz.di

import com.google.gson.GsonBuilder
import com.iium.iium_medioz.util.`object`.Constant.BASE_URL
import com.iium.iium_medioz.util.calendar.UniqueIdentifier
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import kotlin.reflect.KClass


const val USER_ID_PATH_SEGMENT = "__USER_ID_PATH_SEGMENT__"

class ApiFactory @Inject constructor(private val uniqueId: UniqueIdentifier) {
    private val gson = GsonBuilder().create()
    private lateinit var okHttpClient: OkHttpClient

    private val apiRetrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpClient)
            .build()

    fun <T : Any> createApi(clazz: KClass<T>): T = apiRetrofit.create(clazz.java)

    companion object {
        private const val MEDIA_TYPE = "application/json"
        private const val HEADER_TOKEN = "x-access-token"
    }
}