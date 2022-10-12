package com.iium.iium_medioz.viewmodel.main

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.util.common.CommonData
import com.iium.iium_medioz.util.preference.PreferenceManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode

class MainViewModel : BaseObservable() {

    private val comm: CommonData = CommonData().getInstance()
    private val context: Context? = null
    private val pref: PreferenceManager? = null

    // 지도 컨트롤
    internal var isMapReady = false
    private val mapTrackingMode: LocationTrackingMode? = null
    private var isMapTracking = false
    private val currentLatlng: LatLng? = null

    // 마커
    private val mapMarkers: List<MapMarker>? = null
    private var bikeMarkers: List<MapMarker>? = null
    //private ArrayList<LatLng> markerLatLngs; ~ 클러스터링 사용 시 적용
    //private HashMap<String, ArrayList<Marker>> parkingMap; ~ 자전거 마커 미사용


    //private ArrayList<LatLng> markerLatLngs; ~ 클러스터링 사용 시 적용
    //private HashMap<String, ArrayList<Marker>> parkingMap; ~ 자전거 마커 미사용
    // 메인화면 컨트롤 플래그
    private val recentNoticeSeq = 0
    private var isNoticeNotRead = false
    private var isNoticePaused = false

    // 대여소 오버레이
    private var isRentOverlayHide = false
    private var isStationSelected = false
    private val selectedMarker: MapMarker? = null
    private var selectedMarkerDeployRatio = 0
    private val selectedMarkerId: String? = null
    private val selectedStationName: String? = null
    private val stationParkingCount: String? = null

    private var isBikeSelected = false
    var selectedBikeId = ""
    var batteryPercent = ""
    var batteryTime = ""
    var rentBatteryPercent = ""
    var rentBatteryTime = ""


    fun getCurrentLatlng(): LatLng? {
        return currentLatlng
    }

    fun getBikeMarkers(): List<MapMarker?>? {
        return bikeMarkers
    }

    fun setBikeMarkers(bikeMarkers: List<MapMarker?>?) {
        this.bikeMarkers = bikeMarkers as List<MapMarker>?
    }

    @JvmName("getSelectedBikeId1")
    @Bindable
    fun getSelectedBikeId(): String? {
        return selectedBikeId
    }

    @JvmName("setSelectedBikeId1")
    fun setSelectedBikeId(selectedBikeId: String?) {
        this.selectedBikeId = selectedBikeId!!
        notifyChange()
    }

    @JvmName("getBatteryPercent1")
    @Bindable
    fun getBatteryPercent(): String? {
        return batteryPercent
    }

    @JvmName("setBatteryPercent1")
    fun setBatteryPercent(batteryPercent: String?) {
        this.batteryPercent = batteryPercent!!
        notifyChange()
    }

    @JvmName("getBatteryTime1")
    @Bindable
    fun getBatteryTime(): String? {
        return batteryTime
    }

    @JvmName("setBatteryTime1")
    fun setBatteryTime(batteryTime: String?) {
        this.batteryTime = batteryTime!!
        notifyChange()
    }

    @JvmName("getRentBatteryPercent1")
    @Bindable
    fun getRentBatteryPercent(): String? {
        return rentBatteryPercent
    }

    @JvmName("setRentBatteryPercent1")
    fun setRentBatteryPercent(rentBatteryPercent: String?) {
        this.rentBatteryPercent = rentBatteryPercent!!
        notifyChange()
    }

    @JvmName("getRentBatteryTime1")
    @Bindable
    fun getRentBatteryTime(): String? {
        return rentBatteryTime
    }

    @JvmName("setRentBatteryTime1")
    fun setRentBatteryTime(rentBatteryTime: String?) {
        this.rentBatteryTime = rentBatteryTime!!
        notifyChange()
    }

    @Bindable
    fun isBikeSelected(): Boolean {
        return isBikeSelected
    }

    fun setBikeSelected(bikeSelected: Boolean) {
        isBikeSelected = bikeSelected
        notifyChange()
    }

    fun isNoticePaused(): Boolean {
        return isNoticePaused
    }

    fun setNoticePaused(noticePaused: Boolean) {
        isNoticePaused = noticePaused
    }

}