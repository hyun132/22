package com.iium.iium_medioz.model.calendar.weathy

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.annotations.JsonAdapter
import com.iium.iium_medioz.api.ApiSerializer
import java.lang.reflect.Type

@JsonAdapter(CategorySerializer::class)
enum class ClothCategory(val id: Int?=null) {
    TOP(1),
    BOTTOM(2),
    OUTER(3),
    ETC(4);
    companion object {
        fun fromId(id: Int): ClothCategory {
            return values().find {
                it.id == id
            } ?: TOP
        }
    }
}

class CategorySerializer : ApiSerializer<ClothCategory> {
    override fun serialize(src: ClothCategory, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        return JsonPrimitive(src.id)
    }

    override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): ClothCategory {
        return ClothCategory.fromId(json.asInt)
    }
}