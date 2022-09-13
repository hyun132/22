package com.iium.iium_medioz.api

import com.iium.iium_medioz.model.map.MapMarker
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ListAPI {
    // 제휴병원 좌표
    @GET("v1/map/map/search")
    suspend fun getMap(
        @Header("Accesstoken") accesstoken: String?,
        @Query("name") value: String?
    ): MapMarker
}