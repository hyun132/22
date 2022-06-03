package com.iium.iium_medioz.model.rest.base

import com.google.gson.annotations.SerializedName
import io.realm.RealmObject

data class AppPolicy (
    @SerializedName("_id")
    var id: String?=null,
    var result : String?= null,
    var policy : List<Policy>?= null,
    var code : List<Code>?= null,
    var app_menu : List<AppMenu>?= null
)

open class Policy : RealmObject() {
    var id : String?= null
    var name : String?= null
    var value: String?=null
}

open class Code : RealmObject() {
    var cd : String?= null
    var name : String? = null
    var name_en : String? = null
    var category_cd : String?= null
    var category_name : String?= null

    open fun getLocalizeName(langCd: String): String? {
        return if ("en" == langCd) {
            name_en
        } else {
            name
        }
    }
}

open class AppMenu : RealmObject() {
    var cd : String?= null
    var name : String?= null
    var name_en : String?= null
    var category_cd : String?= null
    var category_name : String?= null
    var icon_name : String?= null
    var use_yn : String?= null
}