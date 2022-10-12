package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.repository.DataRepository
import com.iium.iium_medioz.model.send.SendTestModel

class GetSendDataUseCase(private val repository: DataRepository) :
    BaseDataUseCase<SendTestModel> {
    override suspend fun invoke(token: String): Result<SendTestModel> =
        repository.getSend2(token)
}