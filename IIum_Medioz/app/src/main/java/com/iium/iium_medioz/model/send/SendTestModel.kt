package com.iium.iium_medioz.model.send

import com.google.gson.annotations.SerializedName
import com.iium.iium_medioz.model.recycler.DataListSecond

data class SendTestModel (
    val datalist : List<SendList>? =null
)

data class SendList(
    val DataList : List<DataListSecond>? = null,
    val title : String? = null,
    val keyword  : String? = null,
    val timestamp : String? =null,
    val sendcode : String? = null,
    val defaultcode : String? = null,
    val sensitivity : String? = null,
    val userId : String? =null,
    val id : String? =null
)