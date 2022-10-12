package com.iium.iium_medioz.model.datasource

import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.model.calendar.CalendarModel
import com.iium.iium_medioz.model.repository.DataRepository

class GetFeelDataUseCase(private val repository: DataRepository) :
    BaseDataUseCase<CalendarModel> {
    override suspend fun invoke(token: String): Result<CalendarModel> =
        repository.getFeel2(token)

}