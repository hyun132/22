package com.iium.iium_medioz.model

data class TestSendModel(
    val datalist : List<TestDataList>? =null
)

data class TestDataList(
    val id : String? = null,
    val title : String? = null,
    val keyword  : String? = null,
    val timestamp : String? =null,
    val sendcode : String? = null,
    val defaultcode : String? = null,
    val sensitivity : String? = null,
    val dataid : String? = null
)