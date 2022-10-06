package com.iium.iium_medioz.model.calendar.weathy

import com.google.gson.annotations.SerializedName

data class HourlyWeather(
    val time: String? = null,
    val temperature: Int?,
    val pop: Int
)