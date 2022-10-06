package com.iium.iium_medioz.model.test.enumfile

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
    VERY_HOT(1, R.color.icon_angry, "화나요", "화났어요", R.drawable.icon_angry),
    HOT(2, R.color.icon_sad, "슬퍼요", "슬펐어요", R.drawable.icon_sad),
    GOOD(3, R.color.icon_happy, "행복해요", "행복했어요", R.drawable.icon_happy),
    COLD(4, R.color.icon_thanks, "감사해요", "감사했어요", R.drawable.icon_thanks),
    VERY_COLD(5, R.color.icon_suprisei, "놀래요", "놀랬어요", R.drawable.icon_surprise),

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
        return WeatherStamp.fromId(json.asInt) ?: WeatherStamp.GOOD
    }
}