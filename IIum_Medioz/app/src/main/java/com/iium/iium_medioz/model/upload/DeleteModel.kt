package com.iium.iium_medioz.model.upload

import com.google.gson.annotations.SerializedName
import org.bson.types.ObjectId

data class DeleteModel (
    @SerializedName("_id")
    val id : ObjectId? = null
)