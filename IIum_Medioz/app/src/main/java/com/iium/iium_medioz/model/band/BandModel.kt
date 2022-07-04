package com.iium.iium_medioz.model.band

data class BandModel(
    val band : List<BandModeling>? = null
)

data class BandModeling(
    val band_nickname: String? = null,
    val band_title: String? = null,
    val band_keyword: String? = null,
    val band_timestamp: String? = null,
    val band_inquiry: String? = null,
    val band_good: String? = null,
    val band_text: String? = null
)