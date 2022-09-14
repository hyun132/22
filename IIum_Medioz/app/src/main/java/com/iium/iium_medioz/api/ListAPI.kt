package com.iium.iium_medioz.api

import com.iium.iium_medioz.model.map.KaKaoModel
import com.iium.iium_medioz.model.map.MapMarker
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ListAPI {
    // 제휴병원 좌표
    @GET("v2/local/search/keyword.json")
    suspend fun getKaKaoSearch(
        @Header("Authorization") id: String?,
        @Query("query") query: String?,
        @Query("page") page: Int?,
        @Query("size") size: Int?
    ): KaKaoModel


    // 제휴병원 좌표(동네)
    @GET("v1/map/map/search")
    suspend fun getMap(
        @Header("Accesstoken")accesstoken: String?,
        @Query("name")value: String?
    ): MapMarker

    // 제휴병원 좌표(병원)
    @GET("v1/map/map/search/hosiptal")
    suspend fun getHospital(
        @Header("Accesstoken")accesstoken: String?,
        @Query("name")value: String?
    ): MapMarker
}