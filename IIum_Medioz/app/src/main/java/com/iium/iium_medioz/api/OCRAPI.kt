package com.iium.iium_medioz.api

import com.iium.iium_medioz.model.NaverOcrModel
import com.iium.iium_medioz.model.rest.base.Verification
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface OCRAPI {
    // OCR
    @POST
    fun postOCR(@Header("Content-Type") contenttype: String?,
                @Header("X-OCR-SECRET") xocrsecret: String?,
                @Body naverOcrModel: NaverOcrModel): Call<NaverOcrModel>

}