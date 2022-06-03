package com.iium.iium_medioz.model.rest.login

import com.google.gson.annotations.SerializedName

data class LoginResult (
    val phone : String? = null,
    val sex : String? = null,
    val profileImg : String? = null,
    val name : String? = null,
    val terms : String? = null,
    @SerializedName("Accesstoken")
    val accesstoken : String? = null
)

