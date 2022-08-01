package com.iium.iium_medioz.model.calendar

import com.google.gson.annotations.SerializedName


data class DailyWeatherWithInDays(
    val date: Date, @SerializedName("climateIconId") val weather: Weather
) {
    data class Date(
        val year: Int, val month: Int, val day: Int, val dayOfWeek: String
    )
}