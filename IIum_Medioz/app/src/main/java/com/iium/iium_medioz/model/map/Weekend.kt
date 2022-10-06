package com.iium.iium_medioz.model.map

data class Weekend(
    val monday : List<Monday>,
    val tuesday : List<Tuesday>,
    val wednesday : List<Wednesday>,
    val thursday : List<Thursday>,
    val friday : List<Friday>,
    val saturday : List<Saturday>,
    val sunday : List<Sunday>,
)

data class Monday(
    val monday_time_start : String? = null,
    val monday_time_end : String? = null,
    val monday_day_off : Boolean
)

data class Tuesday(
    val tuesday_time_start : String? = null,
    val tuesday_time_end : String? = null,
    val tuesday_day_off : Boolean
)

data class Wednesday(
    val wednesday_time_start : String? = null,
    val wednesday_time_end : String? = null,
    val wednesday_day_off : Boolean
)

data class Thursday(
    val thursday_time_start : String? = null,
    val thursday_time_end : String? = null,
    val thursday_day_off : Boolean
)

data class Friday(
    val friday_time_start : String? = null,
    val friday_time_end : String? = null,
    val friday_day_off : Boolean
)

data class Saturday(
    val saturday_time_start : String? = null,
    val saturday_time_end : String? = null,
    val saturday_day_off : Boolean
)

data class Sunday(
    val sunday_time_start : String? = null,
    val sunday_time_end : String? = null,
    val sunday_day_off : Boolean
)
