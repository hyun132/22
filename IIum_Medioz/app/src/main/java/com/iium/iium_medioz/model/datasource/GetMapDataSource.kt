package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.model.repository.DataRepository
import retrofit2.HttpException
import retrofit2.Response

class GetMapDataSource(private val repository: DataRepository) {
    suspend operator fun invoke(token: String, query: String): Result<MapMarker> =
        repository.getMap2(token, query)
}