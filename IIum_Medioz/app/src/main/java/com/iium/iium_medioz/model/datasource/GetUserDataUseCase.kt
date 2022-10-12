package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.model.repository.DataRepository
import com.iium.iium_medioz.model.rest.login.GetUser

class GetUserDataUseCase(private val repository: DataRepository) {
    suspend operator fun invoke(token: String): Result<GetUser> = repository.getUser2(token)
}