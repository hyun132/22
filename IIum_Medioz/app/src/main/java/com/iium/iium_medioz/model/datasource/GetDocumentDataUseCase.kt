package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.model.document.DocumentListModel
import com.iium.iium_medioz.model.repository.DataRepository

class GetDocumentDataUseCase(
    private val repository: DataRepository
    ) : BaseDataUseCase<DocumentListModel> {
    override suspend fun invoke(token: String): Result<DocumentListModel> =
        repository.getDocument2(token)
}