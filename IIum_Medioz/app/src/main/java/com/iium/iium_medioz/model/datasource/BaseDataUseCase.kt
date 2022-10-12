package com.iium.iium_medioz.model.datasource

interface BaseDataUseCase<T : Any> {
    suspend operator fun invoke(token: String): Result<T>
}