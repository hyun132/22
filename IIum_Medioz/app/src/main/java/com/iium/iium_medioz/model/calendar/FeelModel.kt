package com.iium.iium_medioz.model.calendar

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FeelModel (
    val background: Int = 0,
    val date: String = "",
    val feeling: Int = 1,
    val fileName: MutableList<String> = mutableListOf(),
    val msg: String = "",
    val title: String = "",
    val jurnalId: String = ""
): Parcelable