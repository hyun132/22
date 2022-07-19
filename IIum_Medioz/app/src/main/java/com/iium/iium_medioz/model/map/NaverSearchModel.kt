package com.iium.iium_medioz.model.map

data class NaverSearchModel (
    val lastBuildDate : String? = null,
    val total : Int? = null,
    val start : Int? = null,
    val display : Int? = null,
    val items : List<Items>
)

data class Items(
    val title : String? = null,
    val link : String? = null,
    val category : String? = null,
    val description : String? = null,
    val telephone : String? = null,
    val address : String? = null,
    val roadAddress : String? = null,
    val mapx: Int? = null,
    val mapy : Int? = null

)