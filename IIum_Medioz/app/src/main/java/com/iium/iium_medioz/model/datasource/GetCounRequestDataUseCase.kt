package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.repository.DataRepository
import com.iium.iium_medioz.model.ui.CounGet

class GetCounRequestDataUseCase(private val repository: DataRepository) :
    BaseDataUseCase<CounGet> {
    override suspend fun invoke(token: String): Result<CounGet> = repository.getCounRequest2(token)
}