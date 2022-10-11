package com.iium.iium_medioz.model.feel.entity

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
    @DrawableRes
    val iconRes: Int,
) {
    ANGRY(1, R.color.icon_angry, "너무 더워요", "너무 더웠어요", R.drawable.icon_angry),

    SAD(2, R.color.icon_sad, "더워요", "더웠어요", R.drawable.icon_sad),

    HAPPY(3, R.color.icon_happy, "적당해요", "적당했어요", R.drawable.icon_happy),

    THANKS(4, R.color.icon_thanks, "추워요", "추웠어요", R.drawable.icon_thanks),

    SURPRISE(5, R.color.icon_surprise, "너무 추워요", "너무 추웠어요", R.drawable.icon_surprise),

    ;

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