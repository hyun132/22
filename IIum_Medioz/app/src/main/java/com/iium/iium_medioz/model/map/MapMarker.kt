package com.iium.iium_medioz.model.map

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker

data class MapMarker (
    val result: Boolean? = null,
    val map: List<MapModel>
)

data class MapModel(
    val id: Int? = null,
    val name: String? = null,
    val xvalue: Double? = null,
    val yvalue: Double? = null,
    val address: String? = null,
    val call: String? = null,
    val imgUrl: String? = null
)