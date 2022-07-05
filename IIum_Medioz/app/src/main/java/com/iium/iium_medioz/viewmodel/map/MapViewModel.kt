package com.iium.iium_medioz.viewmodel.map

import android.content.Context
import androidx.databinding.BaseObservable
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.util.preference.PreferenceManager
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode

class MapViewModel : BaseObservable() {

    private val context: Context? = null
    private val pref: PreferenceManager? = null

    // 지도 컨트롤
    internal val isMapReady = false
    private val mapTrackingMode: LocationTrackingMode? = null
    private val isMapTracking = false
    private val currentLatlng: LatLng? = null

    // 마커
    private val mapMarkers: List<MapMarker>? = null
    private val bikeMarkers: List<MapMarker>? = null
    //private ArrayList<LatLng> markerLatLngs; ~ 클러스터링 사용 시 적용
    //private HashMap<String, ArrayList<Marker>> parkingMap; ~ 자전거 마커 미사용


    //private ArrayList<LatLng> markerLatLngs; ~ 클러스터링 사용 시 적용
    //private HashMap<String, ArrayList<Marker>> parkingMap; ~ 자전거 마커 미사용
    // 메인화면 컨트롤 플래그
    private val recentNoticeSeq = 0
    private val isNoticeNotRead = false
    private val isNoticePaused = false

    private val isRentOverlayHide = false
    private val isStationSelected = false
    private val selectedMarker: MapMarker? = null
    private val selectedMarkerDeployRatio = 0
    private val selectedMarkerId: String? = null
    private val selectedStationName: String? = null
    private val stationParkingCount: String? = null

    private val isBikeSelected = false
    var selectedBikeId = ""
    var batteryPercent = ""
    var batteryTime = ""
    var rentBatteryPercent = ""
    var rentBatteryTime = ""
}