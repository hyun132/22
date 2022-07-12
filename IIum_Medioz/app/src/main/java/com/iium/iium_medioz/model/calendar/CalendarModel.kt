package com.iium.iium_medioz.model.calendar

data class CalendarModel (
    val calendarList : List<CalendarFeel>
)

data class CalendarFeel(
    val title: String? = null,
    val feeling: Int? = null,
    val timestamp: String? = null,
    val background: Int? = null,
    val jurnalId: String? = null,
    val devideId: String? = null
)