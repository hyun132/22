package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.recycler.MedicalData
import com.iium.iium_medioz.model.repository.DataRepository

class GetMedicalDataSource(private val repository: DataRepository) {
    suspend operator fun invoke(token: String): Result<MedicalData> =
        repository.getCreateGet2(token)

}