package com.iium.iium_medioz.model.calendar

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.annotations.JsonAdapter
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.ApiSerializer
import java.lang.reflect.Type

@JsonAdapter(WeatherStampSerializer::class)
enum class WeatherStamp(
    val id: Int,
    @ColorRes val colorRes: Int,
    val representation: String,
    val representationPast: String,
    @DrawableRes val iconRes: Int,
) {

    ANGRY(1, R.color.imoji_veryhot, "화나요", "화났어요", R.drawable.icon_angry),
    SAD(
        2, R.color.imoji_hot, "슬퍼요", "슬펐어요", R.drawable.icon_sad),
    HAPPY(
        3, R.color.imoji_good, "행복해요", "행복했어요", R.drawable.icon_happy),
    THANKS(
        4, R.color.imoji_cold, "감사해요", "감사했어요", R.drawable.icon_thanks),
    SUPRISE(5, R.color.imoji_verycold, "놀라요", "놀랐어요", R.drawable.icon_surprise),;

    val index: Int
        get() = values().indexOf(this)

    companion object {
        fun fromId(id: Int?): WeatherStamp? {
            return values().find { it.id == id }
        }

        fun fromIndex(index: Int): WeatherStamp? {
            return values().find { it.index == index }
        }
    }
}

class WeatherStampSerializer : ApiSerializer<WeatherStamp> {
    override fun serialize(src: WeatherStamp?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? {
        return src?.id?.let { JsonPrimitive(it) }
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): WeatherStamp? {
        return WeatherStamp.fromId(json.asInt) ?: WeatherStamp.HAPPY
    }
}