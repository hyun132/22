package com.iium.iium_medioz.model.ui

data class CounGet(
    val userRequest : List<CounList>?=null
)

data class CounList(
    val name : String? = null,
    val title : String? = null,
    val content : String? = null,
    val created: String? = null,
    val terms : String? = null
)