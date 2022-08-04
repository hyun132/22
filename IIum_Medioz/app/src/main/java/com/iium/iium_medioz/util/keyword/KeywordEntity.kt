package com.iium.iium_medioz.util.keyword

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.iium.iium_medioz.util.`object`.Constant.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class KeywordEntity (
    @ColumnInfo(name = "keyword") var keyword: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}