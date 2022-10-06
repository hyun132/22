package com.iium.iium_medioz.model.map

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.overlay.Marker
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
    val imgURL : String?= null,
    val webSite: String?= null,
    val weekend_time: List<Weekend>,
    val detail_words: String?= null,
//    val imgURL_first: String?= null,
//    val imgURL_second: String?= null,
//    val imgURL_third: String?= null,
//    val imgURL_four: String?= null,
//    val imgURL_five: String?= null,
): Serializable, TedClusterItem {
    override fun getTedLatLng(): TedLatLng {
        return TedLatLng(x!!.toDouble(),y!!.toDouble())
    }
}

