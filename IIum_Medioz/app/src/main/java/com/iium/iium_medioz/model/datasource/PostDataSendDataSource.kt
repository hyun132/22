package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.repository.DataRepository
import com.iium.iium_medioz.model.send.DataSend
import com.iium.iium_medioz.model.send.SendModel

class PostDataSendDataSource(private val repository: DataRepository) {
    suspend fun invoke(token: String, data: DataSend): Result<SendModel> =
        repository.getChange2(token, data)
}