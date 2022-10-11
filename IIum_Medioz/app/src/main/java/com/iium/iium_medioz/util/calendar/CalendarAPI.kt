package com.iium.iium_medioz.util.calendar

import com.google.gson.annotations.SerializedName
import com.iium.iium_medioz.model.feel.CalendarPreview
import retrofit2.http.GET
import retrofit2.http.Query

data class FetchCalendarPreviewRes(
    @SerializedName("calendarOverviewList") val list: List<CalendarPreview?>,
    @SerializedName("message") val message: String
)

interface CalendarAPI {
    @GET("users/$USER_ID_PATH_SEGMENT/calendar")
    suspend fun fetchCalendarPreview(
        @Query("start") start: DateString, @Query("end") end: DateString
    ): FetchCalendarPreviewRes
}