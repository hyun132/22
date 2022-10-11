package com.iium.iium_medioz.util.calendar

import android.graphics.Region
import com.google.gson.annotations.SerializedName
import com.iium.iium_medioz.model.feel.entity.WeatherStamp

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

data class DailyWeather(
    val date: Date
)

data class Date(
    val year: Int,
    @SerializedName("month")
    val month: Int,
    @SerializedName("day")
    val day: Int,
    @SerializedName("dayOfWeek")
    val dayOfWeek: String
)

data class HourlyWeather(
    val time: String?,
    val temperature: Int?,
    val pop: Int
)

data class WeathyCloset(
    val top: WeathyClothes?=null,
    val bottom: WeathyClothes?=null,
    val outer: WeathyClothes?=null,
    val etc: WeathyClothes?=null
)

data class WeathyClothes(
    val categoryId: ClothCategory?=null,
    val clothesNum: Int? = null,
    val clothes: List<WeathyCloth>
)

data class WeathyCloth(
    val id: Int,
    val name: String
)