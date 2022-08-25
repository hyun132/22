package com.iium.iium_medioz.model.map

data class KaKaoModel (
    val meta: Meta,
    val documents :  List<Documents>
)

data class Documents(
    val address_name: String? = null,
    val category_group_code: String? = null,
    val category_group_name: String? = null,
    val category_name: String? = null,
    val distance: String? = null,
    val id: String? = null,
    val phone: String? = null,
    val place_name: String? = null,
    val place_url: String? = null,
    val road_address_name: String? = null,
    val x: String? = null,
    val y: String? = null
)

data class Meta(
    val is_end: Boolean,
    val pageable_count: Int,
    val same_name: SameName,
    val total_count: Int
)

data class SameName(
    val keyword: String,
    val region: List<String>,
    val selected_region: String
)