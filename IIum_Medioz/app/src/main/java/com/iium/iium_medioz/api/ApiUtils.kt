package com.iium.iium_medioz.api

import com.iium.iium_medioz.util.`object`.Constant

object ApiUtils {
    val apiService: APIService
    get() = RetrofitClient.getClient(Constant.BASE_URL).create(APIService::class.java)
}