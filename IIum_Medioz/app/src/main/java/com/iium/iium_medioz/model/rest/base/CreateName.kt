package com.iium.iium_medioz.model.rest.base

data class CreateName(
    val result : List<CreateSearch>?=null
)

data class CreateSearch(
    var imgName : String? = null,
    var title : String? = null,
    var keyword : String? = null,
    var detail: String? = null,
    var timestamp : String? = null,
    val defaultcode : String? = null,
    val sensitivity : String? = null,
    val sendcode : String? = null
)