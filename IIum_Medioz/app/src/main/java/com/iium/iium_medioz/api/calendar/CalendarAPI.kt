package com.iium.iium_medioz.api.calendar

import com.google.gson.annotations.SerializedName
import com.iium.iium_medioz.model.test.CalendarPreview
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header


data class FetchCalendarPreviewRes(
    @SerializedName("calendarOverviewList") val list: List<CalendarPreview?>,
    @SerializedName("message") val message: String
)

interface CalendarAPI {
    @GET("v1/calendar/my")
    suspend fun getFeel(@Header("Accesstoken")accesstoken: String?): FetchCalendarPreviewRes
}