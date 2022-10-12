package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.model.repository.DataRepository
import com.iium.iium_medioz.model.upload.DeleteModel
import retrofit2.HttpException
import retrofit2.Response

class GetDataDeleteDataSource(private val repository: DataRepository) {
    suspend fun invoke(token: String, id: String): Result<DeleteModel> =
        repository.getDataDelete2(token, id)
}