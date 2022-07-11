package com.iium.iium_medioz.model.calendar

data class CalendarModel (
    val calendarModel : List<CalendarFeel>
)

data class CalendarFeel(
    val title: String? = null,
    val feeling: String? = null,
    val timestamp: String? = null,
    val background: String? = null,
    val jurnalId: String? = null,
    val devideId: String? = null
)