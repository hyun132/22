package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.repository.DataRepository
import com.iium.iium_medioz.model.upload.ModifyList

class PutModifyDataSource(private val repository: DataRepository) {
    suspend fun invoke(token: String, id: String, list: ModifyList): Result<ModifyList> =
        repository.putData2(token, id, list)
}