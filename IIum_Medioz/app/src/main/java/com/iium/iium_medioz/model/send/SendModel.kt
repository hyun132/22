package com.iium.iium_medioz.model.send

import com.google.gson.annotations.SerializedName

data class SendModel(
    val datalist : List<DataList>? =null
)

data class DataList(
    val id: String? = null,
    val title: String? = null,
    val keyword: String? = null,
    val timestamp: String? =null,
    val firstList: FirstList? = null,
    val secondList: SecondList? = null,
    val thirdList: ThirdList? = null,
    val fourList: FourList? = null,
    val fiveList: FiveList? = null
)

data class FirstList(
    val imgpath: String? = null,
    val sendcode: String? = null,
    val defaultcode: String? = null,
    val sensitivity: String? = null,
    val dataid: String? = null
)

data class SecondList(
    val imgpath: String? = null,
    val sendcode: String? = null,
    val defaultcode: String? = null,
    val sensitivity: String? = null,
    val dataid: String? = null
)

data class ThirdList(
    val imgpath: String? = null,
    val sendcode: String? = null,
    val defaultcode: String? = null,
    val sensitivity: String? = null,
    val dataid: String? = null
)

data class FourList(
    val imgpath: String? = null,
    val sendcode: String? = null,
    val defaultcode: String? = null,
    val sensitivity: String? = null,
    val dataid: String? = null
)

data class FiveList(
    val imgpath: String? = null,
    val sendcode: String? = null,
    val defaultcode: String? = null,
    val sensitivity: String? = null,
    val dataid: String? = null
)
