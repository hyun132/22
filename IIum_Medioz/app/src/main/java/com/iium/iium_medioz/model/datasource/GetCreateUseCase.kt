package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.repository.DataRepository
import com.iium.iium_medioz.model.upload.CreateMedical
import okhttp3.MultipartBody
import okhttp3.RequestBody

class GetCreateUseCase(private val repository: DataRepository) {
    suspend fun invoke(
        token: String,
        datalist: MutableList<MultipartBody.Part?>,
        data: HashMap<String, RequestBody>
    ): Result<CreateMedical> = repository.getCreate2(token, datalist, data)

}