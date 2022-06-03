package com.iium.iium_medioz.model.rest.login

import com.google.gson.annotations.SerializedName

data class LoginSend(
    val phone : String? =null,
    val smsnumber : String? = null,
    val status : String ?= null,

    @SerializedName("Accesstoken")
    val accesstoken : String?=null
)