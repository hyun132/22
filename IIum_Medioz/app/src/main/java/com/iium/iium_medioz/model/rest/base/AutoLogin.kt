package com.iium.iium_medioz.model.rest.base

import com.google.gson.annotations.SerializedName

data class AutoLogin(
    val status : String?=null,

    @SerializedName("Accesstoken")
    val accesstoken : String?=null
)
