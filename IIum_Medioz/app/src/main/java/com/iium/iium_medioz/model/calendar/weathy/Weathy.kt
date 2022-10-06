package com.iium.iium_medioz.model.calendar.weathy

import com.google.gson.annotations.SerializedName
import com.iium.iium_medioz.model.test.enumfile.WeatherStamp

data class Weathy(
    @SerializedName("weathyId") val id: Int,
    val region: Region,
    val dailyWeather: DailyWeather,
    val hourlyWeather: HourlyWeather,
    val closet: WeathyCloset,
    val stampId: WeatherStamp,
    val feedback: String?,
    val imgUrl: String?
)