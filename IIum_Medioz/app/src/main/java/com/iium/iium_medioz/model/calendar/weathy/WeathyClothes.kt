package com.iium.iium_medioz.model.calendar.weathy

data class WeathyClothes(
    val categoryId: ClothCategory?=null,
    val clothesNum: Int? = null,
    val clothes: List<WeathyCloth>
)