package com.iium.iium_medioz.model.upload

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iium.iium_medioz.util.`object`.Constant.TABLE_NAME


data class KeywordEntity (
    var keyword: String
){
    var id: Int = 0
}