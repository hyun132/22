package com.iium.iium_medioz.model.send

import com.google.gson.annotations.SerializedName

data class SendModel(
    val datalist : List<DataList>? =null
)

data class DataList(
    val id : String? = null,
    val title : String? = null,
    val keyword  : String? = null,
    val timestamp : String? =null,
    val sendcode : String? = null,
    val defaultcode : String? = null,
    val sensitivity : String? = null,
    val dataid : String? = null
)
