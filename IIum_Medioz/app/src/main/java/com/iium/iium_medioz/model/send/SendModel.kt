package com.iium.iium_medioz.model.send

import com.google.gson.annotations.SerializedName

data class SendModel(
    val datalist : List<DataSend>? =null
)

data class DataSend(
    val title: String? = null,
    val keyword: String? = null,
    val timestamp: String? = null,
    val sendcode: String? = null,
    val sensitivity: String? = null,
    val dataid: String? = null,
    val defaultcode: String? =null
)

