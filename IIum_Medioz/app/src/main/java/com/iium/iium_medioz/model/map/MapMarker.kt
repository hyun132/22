package com.iium.iium_medioz.model.map

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker

data class MapMarker (
    val result: List<AddressDocument>
)

data class AddressDocument(
    val id: String?= null,
    val address_name : String?= null,
    val place_name: String?= null,
    val road_address_name: String?= null,
    val call: String?= null,
    val x: String?= null,
    val y: String?= null,
    val created: String?= null,
    val imgURL: String?= null

)

