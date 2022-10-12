package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.repository.DataRepository
import com.iium.iium_medioz.model.rest.base.CreateName

class GetSearchDataSource(private val repository: DataRepository) {
    suspend fun invoke(token: String, name: String): Result<CreateName> =
        repository.getSearch2(name, token)
}