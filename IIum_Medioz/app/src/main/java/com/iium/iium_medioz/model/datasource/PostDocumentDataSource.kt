package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.document.DocumentModel
import com.iium.iium_medioz.model.repository.DataRepository

class PostDocumentDataSource(private val repository: DataRepository) {
    suspend fun invoke(token: String, documentModel: DocumentModel): Result<DocumentModel> =
        repository.postDocument2(token, documentModel)
}