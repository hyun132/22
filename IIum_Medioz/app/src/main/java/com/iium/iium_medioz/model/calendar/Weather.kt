package com.iium.iium_medioz.model.calendar

import androidx.annotation.DrawableRes
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.annotations.JsonAdapter
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.ApiSerializer
import java.lang.reflect.Type

@JsonAdapter(WeatherSerizlier::class)
enum class Weather(
    val id: Int,
    @DrawableRes val smallIconId: Int,
    @DrawableRes val bigIconId: Int,
    @DrawableRes val mediumIconId: Int,
    @DrawableRes val HomeBackgroundId: Int,
    @DrawableRes val topBlurId: Int,
    @DrawableRes val SearchBackgroundId: Int,
    @DrawableRes val SearchTopBlurId: Int,
    val backgroundAnimation: BackgroundAnimation? = null,
) {
    CLEAR_SKY(
        1,
        R.drawable.ic_clearsky_day,
        R.drawable.ic_clearsky_day,
        R.drawable.ic_clearsky_day,
        R.drawable.ic_clearsky_day,
        R.drawable.ic_clearsky_day,
        R.drawable.ic_clearsky_day,
        R.drawable.ic_clearsky_day,
    ), ;


    enum class BackgroundAnimation {
        RAIN, SNOW
    }

    companion object {
        fun withId(id: Int?): Weather {
            return values().find { it.id == id } ?: CLEAR_SKY
        }
    }
}

class WeatherSerizlier : ApiSerializer<Weather> {
    override fun serialize(src: Weather, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src.id)
    }

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Weather {
        return Weather.withId(json?.asInt)
    }
}