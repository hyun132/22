package com.iium.iium_medioz.model.map

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker

data class MapMarker (
    val result: List<AddressDocument>
)

data class AddressDocument(
    val id: String?= null,
    val address_name : String?= null,
    val region_1depth_name: String?= null,
    val region_2depth_name: String?= null,
    val region_3depth_name: String?= null,
    val region_4depth_name: String?= null,
    val call: String?= null,
    val x: String?= null,
    val y: String?= null,
    val created: String?= null,
    val imgURL: String?= null
)