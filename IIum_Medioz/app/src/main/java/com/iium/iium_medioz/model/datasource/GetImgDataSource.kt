package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.repository.DataRepository
import okhttp3.ResponseBody

class GetImgDataSource(private val repository: DataRepository) {
    suspend fun invoke(imgSrc: String, token: String): Result<ResponseBody> =
        repository.getImg2(imgSrc, token)
}