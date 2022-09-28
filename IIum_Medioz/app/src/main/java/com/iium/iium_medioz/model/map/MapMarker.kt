package com.iium.iium_medioz.model.map

import ted.gun0912.clustering.clustering.TedClusterItem
import ted.gun0912.clustering.geometry.TedLatLng
import java.io.Serializable

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
): Serializable, TedClusterItem {
    override fun getTedLatLng(): TedLatLng {
        return TedLatLng(x!!.toDouble(),y!!.toDouble())
    }
}