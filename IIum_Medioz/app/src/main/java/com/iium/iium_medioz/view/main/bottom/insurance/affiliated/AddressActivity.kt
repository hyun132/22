package com.iium.iium_medioz.view.main.bottom.insurance.affiliated


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityAddressBinding
import com.iium.iium_medioz.model.map.AddressDocument
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.model.rest.base.Policy
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.KAKAO_MAPX
import com.iium.iium_medioz.util.`object`.Constant.KAKAO_MAPY
import com.iium.iium_medioz.util.`object`.Constant.NAVER_MAPX
import com.iium.iium_medioz.util.`object`.Constant.NAVER_MAPY
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.adapter.map.MapListAdapter
import com.iium.iium_medioz.util.adapter.map.MapViewPagerAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.viewmodel.map.HospitalObservable
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.naver.maps.map.widget.LocationButtonView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mBinding : ActivityAddressBinding
    private lateinit var apiServices: APIService
    private var locationSource: FusedLocationSource? = null
    private var mMap: NaverMap?=null
    private var mViewModel: HospitalObservable?=null

    private var currentLocationButton : LocationButtonView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_address)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        inStatusBar()
        runOnUiThread {
            initView()
//            initAdapter()
        }
    }


    override fun onResume() {
        super.onResume()
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            checkRunTimePermission()
        }
        initMapListener()
    }

    override fun onPause() {
        super.onPause()
        removeGps()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.setIsMainNoticeViewed(false)
        finishAffinity()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveMain()
        finishAffinity()
    }

    private fun initMapListener() {
        if (locationSource == null) {
            locationSource =
                FusedLocationSource(this, Constant.LOCATION_PERMISSION_REQUEST_CODE)
        }
        if (mMap != null) {
            mMap!!.locationSource = locationSource
            mMap!!.locationTrackingMode = LocationTrackingMode.NoFollow
        }
    }

//    private fun initAdapter() {
//        viewPager.adapter = viewPagerAdapter
//
//        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                val selectedHouseModel = viewPagerAdapter.currentList[position]
//                val cameraUpdate = CameraUpdate.scrollTo(LatLng(selectedHouseModel.x!!.toDouble(), selectedHouseModel.y!!.toDouble()))
//                    .animate(CameraAnimation.Easing)
//                mMap?.moveCamera(cameraUpdate)
//            }
//        }) ni.cecha
//    }

    private fun removeGps() {
        setMapTrackingMode(LocationTrackingMode.None)
        locationSource = null
        mMap?.locationSource = null
        mMap?.onMapClickListener = null
    }

    private fun setMapTrackingMode(trackingMode: LocationTrackingMode?) {
        mMap?.locationTrackingMode = trackingMode!!
    }

    private fun checkLocationServicesStatus(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        ))
    }

    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하시겠습니까?")
        builder.setPositiveButton("설정") { _, _ ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, Constant.GPS_ENABLE_REQUEST_CODE)
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }
        builder.setCancelable(false)
        builder.create().show()
    }

    private fun checkRunTimePermission() {
        // 위치 퍼미션 체크
        val hasFineLocationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Constant.PERMISSIONS[0]
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    Constant.PERMISSIONS,
                    Constant.PERMISSION_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    Constant.PERMISSIONS,
                    Constant.PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Constant.GPS_ENABLE_REQUEST_CODE ->
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(Constant.TAG, "onActivityResult : GPS 활성화 되있음")
                        checkRunTimePermission()
                        return
                    }
                }
        }
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(
        permsRequestCode: Int,
        permissions: Array<String>,
        grandResults: IntArray
    ) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults)
        if (permsRequestCode == Constant.PERMISSION_REQUEST_CODE && grandResults.size == Constant.PERMISSIONS.size) {
            var check_result = true
            for (result in grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }
            if (check_result) {

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Constant.PERMISSIONS[0]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        Constant.PERMISSIONS[1]
                    )
                ) {
                    Toast.makeText(
                        this,
                        "권한이 거부되었습니다. 앱을 다시 실행하여 위치 권한을 허용해주세요.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "권한이 거부되었습니다. 설정(앱 정보)에서 위치 권한을 허용해야 합니다. ",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
        }
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status)
    }

    private fun initView() {

        val options = NaverMapOptions()
            .mapType(NaverMap.MapType.Basic)
            .enabledLayerGroups(NaverMap.LAYER_GROUP_BICYCLE)
            .compassEnabled(true)
            .scaleBarEnabled(true)
            .locationButtonEnabled(false)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment?
            ?: MapFragment.newInstance(options).also {
                supportFragmentManager.beginTransaction().add(R.id.map_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }

    override fun onMapReady(naverMap: NaverMap) {
        mMap = naverMap
        naverMap.maxZoom = 18.0
        naverMap.minZoom = 10.0

        val uiSetting = naverMap.uiSettings
        uiSetting.isLocationButtonEnabled = false
        uiSetting.isZoomControlEnabled = false
        uiSetting.isCompassEnabled = false
        uiSetting.isScaleBarEnabled = false
        uiSetting.isRotateGesturesEnabled = false
        uiSetting.isTiltGesturesEnabled = false

        locationSource = FusedLocationSource(this, Constant.LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

//        val overlay = mMap!!.locationOverlay
//        overlay.bearing = 0f
//        overlay.subIconWidth = 0
//        overlay.subIconHeight = 0
//        overlay.iconWidth = resources.getDimensionPixelSize(R.dimen.location_overlay_width)
//        overlay.iconHeight = resources.getDimensionPixelSize(R.dimen.location_overlay_height)
//        overlay.anchor = PointF(0.36f, 0.83f)
//        overlay.icon = OverlayImage.fromResource(R.drawable.icon_new_location)
//        overlay.zIndex = 0
//        overlay.globalZIndex = 0

        getAPI()
    }

    private fun getAPI() {
        LLog.e("제휴병원 좌표")
        val query = ""
        val vercall: Call<MapMarker> = apiServices.getMap(MyApplication.prefs.newaccesstoken, query)
        vercall.enqueue(object : Callback<MapMarker> {
            override fun onResponse(call: Call<MapMarker>, response: Response<MapMarker>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG, "제휴병원 response SUCCESS -> $result")
                    updateMarker(result.result)
                } else {
                    Log.d(LLog.TAG, "제휴병원 response ERROR -> $result")
                    ErrorDialog()
                }
            }

            override fun onFailure(call: Call<MapMarker>, t: Throwable) {
                Log.d(LLog.TAG, "제휴병원 Fail -> ${t.localizedMessage}")
                ErrorDialog()
            }
        })
    }

    private fun updateMarker(result: List<AddressDocument>) {
        result.forEach { maps ->
            val marker = Marker()
            marker.position = LatLng(maps.x!!.toDouble(), maps.y!!.toDouble())
            marker.map = mMap
            marker.icon = OverlayImage.fromResource(R.drawable.icon_marker)
            marker.width = 110
            marker.height = 141
            marker.tag = maps.id
            marker.isHideCollidedMarkers = true
            marker.isForceShowIcon = true
//            marker.setOnClickListener { resut ->
//                if(mBinding.clOverlayParent.visibility == View.GONE) {
//                    viewAnimationAppear(mBinding.clOverlayParent)
//
//                    viewPagerAdapter.currentList.firstOrNull { maps.id == marker.tag }
//                        ?.let {
//                            val position = viewPagerAdapter.currentList.indexOf(it)
//                            viewPager.currentItem = position
//                        }
//
//                } else {
//                    viewAnimationDisappear(mBinding.clOverlayParent)
//                }
//                true
//            }

            marker.onClickListener = Overlay.OnClickListener { overlay: Overlay? ->
                markerClick(maps)
                true
            }

        }
    }

    private fun markerClick(maps: AddressDocument) {
        val id = maps.id.toString()
        val changeZoom = true

        setOverlayViewVisibility(true)

        moveCameraFly(LatLng(maps.x!!.toDouble(), maps.y!!.toDouble()), changeZoom)

        // set overlay info
        updateMarkerInfo(maps)

    }

    private fun moveCameraFly(latLng: LatLng, changeZoom: Boolean) {
        val defaultZoomLevel = 16.0
        val currentZoom =
            if (changeZoom) if (defaultZoomLevel == 0.0) mMap!!.cameraPosition.zoom else defaultZoomLevel else mMap!!.cameraPosition.zoom
        val cameraUpdate =
            CameraUpdate.scrollAndZoomTo(latLng, currentZoom).animate(CameraAnimation.Fly, 350)
        mMap!!.moveCamera(cameraUpdate)
    }

    private fun updateMarkerInfo(mapMarker: AddressDocument) {
        mBinding.viewHospitalItem.tvMapTitle.text = mapMarker.place_name
        mBinding.viewHospitalItem.tvMapCall.text = mapMarker.call
        mBinding.viewHospitalItem.tvMapAddress.text = mapMarker.address_name
        val thumbnailImageView = mBinding.viewHospitalItem.thumbnailImageView
        Glide.with(thumbnailImageView.context)
            .load(mapMarker.imgURL)
            .into(thumbnailImageView)

        mBinding.viewHospitalItem.cardView.setOnClickListener {
            val intent = Intent(this, DocumentActivity::class.java)
            intent.putExtra(Constant.DOCUMENT_NAME, mapMarker.place_name.toString())
            intent.putExtra(Constant.DOCUMENT_ADDRESS, mapMarker.address_name.toString())
            intent.putExtra(Constant.DOCUMENT_CALL, mapMarker.call.toString())
            intent.putExtra(Constant.DOCUMENT_IMGURL, mapMarker.imgURL.toString())
            startActivity(intent)
        }

    }


    private fun setOverlayViewVisibility(visible: Boolean) {
        if (visible) {
            val isAnim = mBinding.clOverlayParent.visibility == View.GONE
            if (isAnim) {
                viewAnimationAppear(mBinding.clOverlayParent)
            }
            mBinding.clOverlayParent.visibility = View.VISIBLE
        } else {
            val isAnim = mBinding.clOverlayParent.visibility == View.VISIBLE
            if (isAnim) {
                viewAnimationDisappear(mBinding.clOverlayParent)
            }
            mBinding.clOverlayParent.visibility = View.GONE
        }

    }


    private fun viewAnimationAppear(v: View) {
        LLog.e("viewAnimationAppear")
        v.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_down_to_top_slide)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                v.clearAnimation()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        v.startAnimation(animation)
    }

    private fun viewAnimationDisappear(v: View) {
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_top_to_down_slide)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                v.clearAnimation()
                v.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        v.startAnimation(animation)
    }

    private val mCameraIdleListener = NaverMap.OnCameraIdleListener {
        var isBikeMarkerShow = true

        val bikeShow = getBikeMarkerVisibilty()
        val change = isBikeMarkerShow != bikeShow
        if (change) {
            setBikeMarkerVisibility(bikeShow)
            isBikeMarkerShow = bikeShow
        }
    }

    private fun setBikeMarkerVisibility(visibility: Boolean) {
        val mapMarkers = mViewModel!!.getBikeMarkers() ?: return
        for (mapMarker in mapMarkers) {
            mapMarker?.result
        }
    }

    private fun getBikeMarkerVisibilty(): Boolean {
        var markerVisibleZoomLevel = 0.0

        if (markerVisibleZoomLevel == 0.0) {
            val zoomPolicy =
                realm.where(Policy::class.java).equalTo("id", "APP_ZOOM_LEVEL").findFirst()
            markerVisibleZoomLevel = zoomPolicy?.value?.toDouble() ?: 10.0
        }
        val currentZoomLevel = mMap!!.cameraPosition.zoom
        return markerVisibleZoomLevel <= currentZoomLevel
    }

    fun onSearchClick(v: View) {
        searchAddress()
    }

    fun onlistClick(v: View) {

    }

    fun onlocationClick(v: View) {
        currentLocationButton?.map = mMap
    }

    fun onBackPressed(v: View) {
        moveMain()
    }
}