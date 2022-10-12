package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.repository.DataRepository
import okhttp3.ResponseBody

class GetProfileImageDataSource(private val repository: DataRepository) {
    suspend fun invoke(profile: String, token: String): Result<ResponseBody> =
        repository.getProfileImg2(profile, token)

}