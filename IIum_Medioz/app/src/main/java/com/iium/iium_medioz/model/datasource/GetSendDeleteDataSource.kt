package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.repository.DataRepository
import com.iium.iium_medioz.model.upload.DeleteModel

class GetSendDeleteDataSource(private val repository: DataRepository) {
    suspend fun invoke(token: String, id: String): Result<DeleteModel> =
        repository.getSendDelete2(token, id)
}