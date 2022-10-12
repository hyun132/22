package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.recycler.MedicalData
import com.iium.iium_medioz.model.repository.DataRepository

class GetCreateGetDataUseCase(private val repository: DataRepository) :
    BaseDataUseCase<MedicalData> {
    override suspend fun invoke(token: String): Result<MedicalData> =
        repository.getCreateGet2(token)
}