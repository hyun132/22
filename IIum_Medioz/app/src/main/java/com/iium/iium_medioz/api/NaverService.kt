package com.iium.iium_medioz.api

import com.iium.iium_medioz.model.map.NaverSearchModel
import com.iium.iium_medioz.model.rest.base.Verification
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NaverService {
    // 네이버 검색 API
    @GET("v1/search/local.json")
    fun getNaverSearch(@Header("X-Naver-Client-Id")id: String?,
                       @Header("X-Naver-Client-Secret")pw: String?,
                       @Query("query") query: String?,
                       @Query("display") display: Int?,
                       @Query("start") start: Int?,
                       @Query("sort")random:String?): Call<NaverSearchModel>
}