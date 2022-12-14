package com.iium.iium_medioz.view.main.bottom.insurance.affiliated

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityHospitalBinding
import com.iium.iium_medioz.model.map.AddressDocument
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.DETAIL_WORDS
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_IMGURL
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_WEBSITE
import com.iium.iium_medioz.util.`object`.Constant.FRIDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.FRIDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.FRIDAY_TIME_START
import com.iium.iium_medioz.util.`object`.Constant.GPS_ENABLE_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.LOCATION_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.MONDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.MONDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.MONDAY_TIME_START
import com.iium.iium_medioz.util.`object`.Constant.PERMISSIONS
import com.iium.iium_medioz.util.`object`.Constant.PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.SATURDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.SATURDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.SATURDAY_TIME_START
import com.iium.iium_medioz.util.`object`.Constant.SUNDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.SUNDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.SUNDAY_TIME_START
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.`object`.Constant.THURSDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.THURSDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.THURSDAY_TIME_START
import com.iium.iium_medioz.util.`object`.Constant.TUESDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.TUESDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.TUESDAY_TIME_START
import com.iium.iium_medioz.util.`object`.Constant.WEDNESDAY_DAY_OFF
import com.iium.iium_medioz.util.`object`.Constant.WEDNESDAY_TIME_END
import com.iium.iium_medioz.util.`object`.Constant.WEDNESDAY_TIME_START
import com.iium.iium_medioz.util.activity.setOnSingleClickListener
import com.iium.iium_medioz.util.adapter.map.MapListAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.viewmodel.main.insurance.affiliated.HospitalViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ted.gun0912.clustering.naver.TedNaverClustering

class HospitalActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var mBinding: ActivityHospitalBinding
    private lateinit var apiServices: APIService
    private var locationSource: FusedLocationSource? = null
    private var mMap: NaverMap?=null
    private var currentLatLng : LatLng = LatLng(0.0, 0.0)
    var mapFragment : MapFragment = MapFragment() // onResume?????? ???????????? ?????????
    private val viewModel: HospitalViewModel by viewModel()


    val bottomSheet: ConstraintLayout by lazy{
        findViewById(R.id.bottom_sheet_include)
    }
    lateinit var sheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val recyclerView : RecyclerView by lazy {
        findViewById(R.id.map_re)
    }

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null // ?????? ????????? ???????????? ?????? ??????
    lateinit var mLastLocation: Location // ?????? ?????? ????????? ?????? ??????
    private lateinit var mLocationRequest: LocationRequest // ?????? ?????? ????????? ??????????????? ????????????

    private val recyclerViewAdapter = MapListAdapter(OKClickListener = {
        val intent = Intent(this, HospitalInFoActivity::class.java)

        intent.apply {
            intent.putExtra(Constant.DOCUMENT_NAME, it.place_name.toString())
            intent.putExtra(Constant.DOCUMENT_ADDRESS, it.address_name)
            intent.putExtra(Constant.DOCUMENT_CALL, it.call.toString())
            intent.putExtra(DOCUMENT_IMGURL, it.imgURL.toString())
            intent.putExtra(DOCUMENT_WEBSITE, it.webSite.toString())

            intent.putExtra(SUNDAY_TIME_START, it.weekend_time.map { it -> it.sunday.map { it.sunday_time_start } }.toString())
            intent.putExtra(SUNDAY_TIME_END, it.weekend_time.map { it -> it.sunday.map { it.sunday_time_end } }.toString())
            intent.putExtra(SUNDAY_DAY_OFF, it.weekend_time.map { it -> it.sunday.map { it.sunday_day_off } }.toString())

            intent.putExtra(MONDAY_TIME_START, it.weekend_time.map { it -> it.monday.map { it.monday_time_start } }.toString())
            intent.putExtra(MONDAY_TIME_END, it.weekend_time.map { it -> it.monday.map { it.monday_time_end } }.toString())
            intent.putExtra(MONDAY_DAY_OFF, it.weekend_time.map { it -> it.monday.map { it.monday_day_off } }.toString())

            intent.putExtra(TUESDAY_TIME_START, it.weekend_time.map { it -> it.tuesday.map { it.tuesday_time_start } }.toString())
            intent.putExtra(TUESDAY_TIME_END, it.weekend_time.map { it -> it.tuesday.map { it.tuesday_time_end } }.toString())
            intent.putExtra(TUESDAY_DAY_OFF, it.weekend_time.map { it -> it.tuesday.map { it.tuesday_day_off } }.toString())

            intent.putExtra(WEDNESDAY_TIME_START, it.weekend_time.map { it -> it.wednesday.map { it.wednesday_time_start } }.toString())
            intent.putExtra(WEDNESDAY_TIME_END, it.weekend_time.map { it -> it.wednesday.map { it.wednesday_time_end } }.toString())
            intent.putExtra(WEDNESDAY_DAY_OFF, it.weekend_time.map { it -> it.wednesday.map { it.wednesday_day_off } }.toString())

            intent.putExtra(THURSDAY_TIME_START, it.weekend_time.map { it -> it.thursday.map { it.thursday_time_start } }.toString())
            intent.putExtra(THURSDAY_TIME_END, it.weekend_time.map { it -> it.thursday.map { it.thursday_time_end } }.toString())
            intent.putExtra(THURSDAY_DAY_OFF, it.weekend_time.map { it -> it.thursday.map { it.thursday_day_off } }.toString())

            intent.putExtra(FRIDAY_TIME_START, it.weekend_time.map { it -> it.friday.map { it.friday_time_start } }.toString())
            intent.putExtra(FRIDAY_TIME_END, it.weekend_time.map { it -> it.friday.map { it.friday_time_end } }.toString())
            intent.putExtra(FRIDAY_DAY_OFF, it.weekend_time.map { it -> it.friday.map { it.friday_day_off } }.toString())

            intent.putExtra(SATURDAY_TIME_START, it.weekend_time.map { it -> it.saturday.map { it.saturday_time_start } }.toString())
            intent.putExtra(SATURDAY_TIME_END, it.weekend_time.map { it -> it.saturday.map { it.saturday_time_end } }.toString())
            intent.putExtra(SATURDAY_DAY_OFF, it.weekend_time.map { it -> it.saturday.map { it.saturday_day_off } }.toString())

            intent.putExtra(DETAIL_WORDS, it.detail_words.toString())

//            intent.putExtra(Constant.DOCUMENT_IMGURL_FIRST, it.imgURL_first.toString())
//            intent.putExtra(Constant.DOCUMENT_IMGURL_SECOND, it.imgURL_second.toString())
//            intent.putExtra(Constant.DOCUMENT_IMGURL_THIRD, it.imgURL_third.toString())
//            intent.putExtra(Constant.DOCUMENT_IMGURL_FOUR, it.imgURL_four.toString())
//            intent.putExtra(Constant.DOCUMENT_IMGURL_FIVE, it.imgURL_five.toString())
        }
        startActivity(intent)
    },itemClickListener = {
        val cameraUpdate = CameraUpdate.scrollAndZoomTo(LatLng(it.x!!.toDouble(), it.y!!.toDouble()),18.0).animate(CameraAnimation.Fly, 1000)
        mMap!!.moveCamera(cameraUpdate)
        sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_hospital)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        sheetBehavior = BottomSheetBehavior.from(bottomSheet)

        mLocationRequest =  LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        runOnUiThread {
            initAdapter()
            initView()
            inStatusBar()
        }

    }

    override fun onResume() {
        super.onResume()
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            checkRunTimePermission()
        }
//        mapFragment.getMapAsync(this) // ???????????? NaverMap ????????? ?????????. NaverMap ????????? ???????????? callback??? OnMapReadyCallback.onMapReady(NaverMap)??? ?????????.

        viewModel.ismapReady.observe(this, Observer{
            if(it) initMapListener()
        }) // ??????: map??? ??????????????? initMapListener ??????
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

    private fun initMapListener() { // ??????
        if (locationSource == null) {
            //Log.e("test", "initMapListener, $locationSource")
            locationSource =
                FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
            //Log.e("test", "initMapListener2, $locationSource")
        }
        if (mMap != null && comm != null) {
            //Log.e("test", "initMapListener3, ${locationSource!!.lastLocation}")
            mMap!!.locationSource = locationSource
            mMap!!.addOnLocationChangeListener(locationChangeListener)
            mMap!!.locationTrackingMode = LocationTrackingMode.Follow
        }
    }

    private fun removeGps() {
        if(mMap != null){
            setMapTrackingMode(LocationTrackingMode.None)
            locationSource = null
            mMap?.locationSource = null
            mMap?.onMapClickListener = null
        }
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
        builder.setTitle("?????? ????????? ????????????")
        builder.setMessage("?????? ???????????? ???????????? ?????? ???????????? ???????????????.\n" + "?????? ????????? ?????????????????????????")
        builder.setPositiveButton("??????") { _, _ ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        }
        builder.setNegativeButton("??????") { dialog, _ ->
            dialog.cancel()
        }
        builder.setCancelable(false)
        builder.create().show()
    }

    private fun checkRunTimePermission() {
        // ?????? ????????? ??????
        val hasFineLocationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    PERMISSIONS[0]
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    PERMISSION_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            GPS_ENABLE_REQUEST_CODE ->
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d(TAG, "onActivityResult : GPS ????????? ?????????")
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
        if (permsRequestCode == PERMISSION_REQUEST_CODE && grandResults.size == PERMISSIONS.size) {
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
                        PERMISSIONS[0]
                    )
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        PERMISSIONS[1]
                    )
                ) {
                    Toast.makeText(
                        this,
                        "????????? ?????????????????????. ?????? ?????? ???????????? ?????? ????????? ??????????????????.",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "????????? ?????????????????????. ??????(??? ??????)?????? ?????? ????????? ???????????? ?????????. ",
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

    private fun initAdapter() { // 2
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initView() { // 3
        if (locationSource == null) {
            //Log.e("test", "initView, $locationSource")
            locationSource =
                FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
            //Log.e("test", "initView2, ${locationSource!!.lastLocation}")
        }

        // ??????: mapFragment??? null??? ?????? ?????? ????????????, ????????? position??? ????????? ???????????? ??????
        val fm = supportFragmentManager
        var mapFragment = fm.findFragmentById(R.id.map_fragment) as MapFragment?
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(
                NaverMapOptions().minZoom(10.0)
                    .camera(
                        CameraPosition(
                            LatLng(36.446555, 127.119055),
                            15.0
                        )
                    )
                    .mapType(NaverMap.MapType.Basic) // ????????? ????????? ??????
                    .enabledLayerGroups(NaverMap.LAYER_GROUP_BICYCLE) // ???????????? ????????? ????????? ????????? ??????
                    .compassEnabled(true) // ???????????? ??????????????? ??????
                    .scaleBarEnabled(true) // ?????? ?????? ??????????????? ??????
            )
            fm.beginTransaction().add(R.id.map_fragment, mapFragment).commit()
        }
        mapFragment!!.getMapAsync(this)
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
        mMap!!.maxZoom = 18.0
        mMap!!.minZoom = 10.0

        viewModel.setIsMapReady(true)

        val uiSetting = mMap!!.uiSettings
        uiSetting.isZoomControlEnabled = false

        mMap!!.setOnMapClickListener{ point, coord ->
            viewAnimationDisappear(mBinding.clOverlayParent)
            bottomSheet.visibility = View.GONE
        } // ?????? ?????? ??? ????????????

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        val kakao_mapx = intent.getStringExtra(Constant.KAKAO_MAPX)
        val kakap_mapy = intent.getStringExtra(Constant.KAKAO_MAPY)

        if(kakao_mapx!=null && kakap_mapy!=null) {
            val cameraUpdate: CameraUpdate = CameraUpdate.scrollTo(LatLng(kakao_mapx.toDouble(),kakap_mapy.toDouble()))
            naverMap.moveCamera(cameraUpdate)
        } else {
            mMap!!.locationSource = locationSource
            setMapTrackingMode(LocationTrackingMode.Follow)
            mMap!!.addOnLocationChangeListener(locationChangeListener)

            locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
            naverMap.locationSource = locationSource
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        }

        viewModel.locations.observe(this) {
            updateMarker(it)
            recyclerViewAdapter.submitList(it)
        }
        initMapListener()
    }

    private var locationChangeListener = NaverMap.OnLocationChangeListener { location ->
        currentLatLng = LatLng(location.latitude, location.longitude)
    }

    private fun updateMarker(result: List<AddressDocument>) {
        result.forEach { maps ->
            // ?????? ???????????????
            TedNaverClustering.with<AddressDocument>(this, mMap!!)
                .customMarker { // ????????? ????????? ???????????? ??????
                    Marker().apply {
                        position = LatLng(maps.x!!.toDouble(), maps.y!!.toDouble())
                        map = mMap
                        icon = OverlayImage.fromResource(R.drawable.icon_marker)
                        width = 110
                        height = 141
                        tag = maps.id
                        isHideCollidedMarkers = true
                        isForceShowIcon = false
                    }
                }.minClusterSize(1)
                .markerClickListener  { resut->
                    viewAnimationAppear(mBinding.clOverlayParent)
                    val result_place = result.filter { it.id == resut.id }[0]
                    mBinding.viewHospitalItem.tvMapTitle.text = result_place.place_name.toString()
                    mBinding.viewHospitalItem.tvMapAddress.text = result_place.address_name.toString()
                    mBinding.viewHospitalItem.tvMapCall.text = result_place.call.toString()
                    sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    val thumbnailImageView = mBinding.viewHospitalItem.thumbnailImageView
                    Glide.with(thumbnailImageView.context)
                        .load(result_place.imgURL)
                        .transform(CenterCrop())
                        .into(thumbnailImageView)

                    mBinding.viewHospitalItem.cardView.setOnSingleClickListener {
                        val intent = Intent(this, HospitalInFoActivity::class.java)

                        intent.apply {
                            intent.putExtra(Constant.DOCUMENT_NAME, result_place.place_name.toString())
                            intent.putExtra(Constant.DOCUMENT_ADDRESS, result_place.address_name.toString())
                            intent.putExtra(Constant.DOCUMENT_CALL,result_place.call.toString())
                            intent.putExtra(DOCUMENT_IMGURL, result_place.imgURL.toString())
                            intent.putExtra(DOCUMENT_WEBSITE, result_place.webSite.toString())

                            intent.putExtra(SUNDAY_TIME_START, result_place.weekend_time.map { it -> it.sunday.map { it.sunday_time_start } }.toString())
                            intent.putExtra(SUNDAY_TIME_END, result_place.weekend_time.map { it -> it.sunday.map { it.sunday_time_end } }.toString())
                            intent.putExtra(SUNDAY_DAY_OFF, result_place.weekend_time.map { it -> it.sunday.map { it.sunday_day_off } }.toString())

                            intent.putExtra(MONDAY_TIME_START, result_place.weekend_time.map { it -> it.monday.map { it.monday_time_start } }.toString())
                            intent.putExtra(MONDAY_TIME_END, result_place.weekend_time.map { it -> it.monday.map { it.monday_time_end } }.toString())
                            intent.putExtra(MONDAY_DAY_OFF, result_place.weekend_time.map { it -> it.monday.map { it.monday_day_off } }.toString())

                            intent.putExtra(TUESDAY_TIME_START, result_place.weekend_time.map { it -> it.tuesday.map { it.tuesday_time_start } }.toString())
                            intent.putExtra(TUESDAY_TIME_END, result_place.weekend_time.map { it -> it.tuesday.map { it.tuesday_time_end } }.toString())
                            intent.putExtra(TUESDAY_DAY_OFF, result_place.weekend_time.map { it -> it.tuesday.map { it.tuesday_day_off } }.toString())

                            intent.putExtra(WEDNESDAY_TIME_START, result_place.weekend_time.map { it -> it.wednesday.map { it.wednesday_time_start } }.toString())
                            intent.putExtra(WEDNESDAY_TIME_END, result_place.weekend_time.map { it -> it.wednesday.map { it.wednesday_time_end } }.toString())
                            intent.putExtra(WEDNESDAY_DAY_OFF, result_place.weekend_time.map { it -> it.wednesday.map { it.wednesday_day_off } }.toString())

                            intent.putExtra(THURSDAY_TIME_START, result_place.weekend_time.map { it -> it.thursday.map { it.thursday_time_start } }.toString())
                            intent.putExtra(THURSDAY_TIME_END, result_place.weekend_time.map { it -> it.thursday.map { it.thursday_time_end } }.toString())
                            intent.putExtra(THURSDAY_DAY_OFF, result_place.weekend_time.map { it -> it.thursday.map { it.thursday_day_off } }.toString())

                            intent.putExtra(FRIDAY_TIME_START, result_place.weekend_time.map { it -> it.friday.map { it.friday_time_start } }.toString())
                            intent.putExtra(FRIDAY_TIME_END, result_place.weekend_time.map { it -> it.friday.map { it.friday_time_end } }.toString())
                            intent.putExtra(FRIDAY_DAY_OFF, result_place.weekend_time.map { it -> it.friday.map { it.friday_day_off } }.toString())

                            intent.putExtra(SATURDAY_TIME_START, result_place.weekend_time.map { it -> it.saturday.map { it.saturday_time_start } }.toString())
                            intent.putExtra(SATURDAY_TIME_END, result_place.weekend_time.map { it -> it.saturday.map { it.saturday_time_end } }.toString())
                            intent.putExtra(SATURDAY_DAY_OFF, result_place.weekend_time.map { it -> it.saturday.map { it.saturday_day_off } }.toString())

                            intent.putExtra(DETAIL_WORDS, result_place.detail_words.toString())

                        }
                        startActivity(intent)
                    }
                }
                .items(result)
                .make()
        }
    }

    private fun viewAnimationAppear(v: View) {
        LLog.e("viewAnimationAppear")
        v.visibility = View.VISIBLE
        val animation = AnimationUtils.loadAnimation(this, R.anim.anim_down_to_top_slide)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }
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
            override fun onAnimationStart(animation: Animation) {

            }
            override fun onAnimationEnd(animation: Animation) {

                v.clearAnimation()
                v.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        v.startAnimation(animation)
    }


    fun onSearchClick(v: View) {
        searchAddress()
    }

    fun onlistClick(v: View) {
        bottomSheet.visibility = View.VISIBLE
        sheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    fun onlocationClick(v: View) {
        val kakao_mapx = intent.getStringExtra(Constant.KAKAO_MAPX)
        val kakap_mapy = intent.getStringExtra(Constant.KAKAO_MAPY)

        if(kakao_mapx!=null && kakap_mapy!=null) {
            startLocationUpdates()
            return
        } else {
            val cameraUpdate = CameraUpdate.scrollTo(currentLatLng)
                .animate(CameraAnimation.Fly, 1000)
            mMap!!.moveCamera(cameraUpdate)
        }
    }

    private fun startLocationUpdates() {
        //FusedLocationProviderClient??? ??????????????? ??????.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        // ????????? ????????? ?????? ?????? ??????????????? ???????????? ????????? ??????
        // ????????? ?????? ?????????(Looper.myLooper())?????? ??????(mLocationCallback)?????? ?????? ??????????????? ??????
        mFusedLocationProviderClient!!.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper())
    }

    // ??????????????? ?????? ?????? ????????? ???????????? ??????
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // ??????????????? ?????? location ????????? onLocationChanged()??? ??????
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation!!)
        }
    }

    fun onLocationChanged(location: Location) {
        mLastLocation = location
        val cameraLatLng = LatLng(mLastLocation.latitude, mLastLocation.longitude)

        val cameraUpdate = CameraUpdate.scrollTo(cameraLatLng)
            .animate(CameraAnimation.Fly, 1000)

        mMap!!.moveCamera(cameraUpdate)
    }

    fun onBackPressed(v: View) {
        moveMain()
    }
}
