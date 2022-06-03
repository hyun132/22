package com.iium.iium_medioz.model.ui

data class NoticeModel(
    val announcement : List<Announcement>? = null
)

data class Announcement(
    val id : String? = null,
    val title : String? = null,
    val content : String? = null,
    val created : String? = null
)