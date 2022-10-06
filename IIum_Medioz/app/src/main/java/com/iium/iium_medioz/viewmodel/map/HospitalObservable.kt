package com.iium.iium_medioz.viewmodel.map

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.util.common.CommonData
import com.iium.iium_medioz.util.preference.PreferenceManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.overlay.Marker

class HospitalObservable() : BaseObservable() {

    private val comm: CommonData = CommonData().getInstance()
    private val context: Context? = null
    private val pref: PreferenceManager? = null

    // 지도 컨트롤
    private var isMapReady = false
    private var mapTrackingMode: LocationTrackingMode? = null
    private var isMapTracking = false
    private var currentLatlng: LatLng? = null

    // 마커
    private var mapMarkers: List<MapMarker>? = null
    private var bikeMarkers: List<MapMarker>? = null

    // 대여소 오버레이
    private val isRentOverlayHide = false
    private var isStationSelected = false
    private var selectedMarker: MapMarker? = null
    private var selectedMarkerDeployRatio = 0
    private var selectedMarkerId: String? = null
    private var selectedStationName: String? = null
    private var stationParkingCount: String? = null

    private var isBikeSelected = false
    var selectedBikeId = ""
    var batteryPercent = ""
    var batteryTime = ""
    var rentBatteryPercent = ""
    var rentBatteryTime = ""
    private val userLangCd: String? = null

    fun isMapReady(): Boolean {
        return isMapReady
    }

    fun setMapReady(mapReady: Boolean) {
        isMapReady = mapReady
    }

    fun getMapTrackingMode(): LocationTrackingMode? {
        return mapTrackingMode
    }

    fun setMapTrackingMode(mapTrackingMode: LocationTrackingMode?) {
        this.mapTrackingMode = mapTrackingMode
    }

    fun setMapTracking(mapTracking: Boolean) {
        isMapTracking = mapTracking
    }

    @Bindable
    fun isStationSelected(): Boolean {
        return isStationSelected
    }

    fun setStationSelected(stationSelected: Boolean) {
        isStationSelected = stationSelected
        notifyChange()
    }

    @Bindable
    fun getSelectedStationName(): String? {
        return selectedStationName
    }

    fun setSelectedStationName(selectedStationName: String?) {
        this.selectedStationName = selectedStationName
        notifyChange()
    }

    fun getMapMarkers(): List<MapMarker?>? {
        return mapMarkers
    }

    @Bindable
    fun getSelectedMarker(): MapMarker? {
        return selectedMarker
    }


    @Bindable
    fun getSelectedMarkerId(): String? {
        return selectedMarkerId
    }

    fun setSelectedMarkerId(selectedMarkerId: String?) {
        this.selectedMarkerId = selectedMarkerId
        notifyChange()
    }

    fun getCurrentLatlng(): LatLng? {
        return currentLatlng
    }

    fun setCurrentLatlng(currentLatlng: LatLng?) {
        this.currentLatlng = currentLatlng
    }

    fun getBikeMarkers(): List<MapMarker?>? {
        return bikeMarkers
    }

    @Bindable
    fun isBikeSelected(): Boolean {
        return isBikeSelected
    }

    fun setBikeSelected(bikeSelected: Boolean) {
        isBikeSelected = bikeSelected
        notifyChange()
    }

    @Bindable
    fun isRent(): Boolean {
        return isRent()
    }
}