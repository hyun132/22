package com.iium.iium_medioz.model.rest.login

import com.google.gson.annotations.SerializedName

data class GetUser(
    val user : UserGet? = null
)

data class UserGet(
    @SerializedName("_id")
    val _id : String? = null,
    val imgName : String? = null,
    val phone : String? = null,
    val sex : String? = null,
    val name : String? = null,
    val admin : Boolean? = null,
    val pickscore : String? = null,
    val videoscore : String? = null,
    val keywordscore : String? = null,
    val sensitivityscore : String? = null,
    val allscore : String? = null,
    val id : String? = null
)