package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.OCRModel
import com.iium.iium_medioz.model.repository.DataRepository

class PostOCRDataSource(private val repository: DataRepository) {
    suspend fun invoke(token: String, data: OCRModel): Result<OCRModel> =
        repository.postOCR2(token, data)
}