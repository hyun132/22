package com.iium.iium_medioz.model.map

data class EqulasSearchModel (
    val documents : List<Documentss>,
    val meta : Metas
    )

data class Documentss(
    val address : Address,
    val address_name : String? = null,
    val address_type: String?= null,
    val x: String?= null,
    val y: String?= null
)

data class Address(
    val address_name: String? = null,
    val b_code: String? = null,
    val h_code: String?= null,
    val main_address_no: String?= null,
    val mountain_yn: String? = null,
    val region_1depth_name: String?= null,
    val region_2depth_name: String?= null,
    val region_3depth_h_name: String?= null,
    val region_3depth_name: String?= null,
    val sub_address_no: String?= null,
    val x: String?= null,
    val y: String?= null
)

data class Metas(
    val is_end: Boolean,
    val pageable_count : Int? = null,
    val total_count: Int?= null
)