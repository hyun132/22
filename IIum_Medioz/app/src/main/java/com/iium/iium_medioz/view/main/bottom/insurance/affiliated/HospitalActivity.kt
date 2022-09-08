package com.iium.iium_medioz.view.main.bottom.insurance.affiliated

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityHospitalBinding
import com.iium.iium_medioz.model.map.AddressDocument
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_ADDRESS
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_CALL
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_IMGURL
import com.iium.iium_medioz.util.`object`.Constant.DOCUMENT_NAME
import com.iium.iium_medioz.util.`object`.Constant.GPS_ENABLE_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.LOCATION_PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.NAVER_MAPX
import com.iium.iium_medioz.util.`object`.Constant.NAVER_MAPY
import com.iium.iium_medioz.util.`object`.Constant.PERMISSIONS
import com.iium.iium_medioz.util.`object`.Constant.PERMISSION_REQUEST_CODE
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.adapter.map.MapListAdapter
import com.iium.iium_medioz.util.adapter.map.MapViewPagerAdapter
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.naver.maps.map.widget.LocationButtonView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HospitalActivity : BaseActivity(), OnMapReadyCallback, Overlay.OnClickListener {

    private lateinit var mBinding : ActivityHospitalBinding
    private lateinit var apiServices: APIService

    private val viewPager : ViewPager2 by lazy {
        findViewById(R.id.houseViewPager)
    }
    private val recyclerView : RecyclerView by lazy {
        findViewById(R.id.map_re)
    }
    private val currentLocationButton : LocationButtonView by lazy {
        findViewById(R.id.currentLocationButton)
    }

    private var locationSource: FusedLocationSource? = null
    private var mMap: NaverMap?=null

    private val recyclerViewAdapter = MapListAdapter(itemClickListener = {
        val intent = Intent(this, DocumentActivity::class.java)

        intent.apply {
            intent.putExtra(DOCUMENT_NAME, it.address_name.toString())
            intent.putExtra(DOCUMENT_ADDRESS, "${it.region_1depth_name} ${it.region_2depth_name} ${it.region_3depth_name} ${it.region_4depth_name}")
            intent.putExtra(DOCUMENT_CALL, it.call.toString())
            intent.putExtra(DOCUMENT_IMGURL, it.imgURL.toString())
        }
            startActivity(intent)
    })

    private val viewPagerAdapter = MapViewPagerAdapter(itemClickListener = {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "[지금 이 가격에 예약하세요!!] ${it.address_name} ${it.region_1depth_name} 사진보기 : ${it.imgURL}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_hospital)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this

        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }

        initAdapter()
        inStatusBar()
        initView()
    }

    override fun onResume() {
        super.onResume()
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()
        } else {
            checkRunTimePermission()
        }
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
            LocationManager.NETWORK_PROVIDER))
    }

    private fun showDialogForLocationServiceSetting() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n" + "위치 설정을 수정하시겠습니까?")
        builder.setPositiveButton("설정") { _, _ ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE)
        }
        builder.setNegativeButton("취소") { dialog, _ ->
            dialog.cancel()
        }
        builder.setCancelable(false)
        builder.create().show()
    }

    private fun checkRunTimePermission() {
        // 위치 퍼미션 체크
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    PERMISSIONS[0]
                )) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE)
            }
            else {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE)
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
                        Log.d(TAG, "onActivityResult : GPS 활성화 되있음")
                        checkRunTimePermission()
                        return
                    }
                }
        }
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                //result.getContents 를 이용 데이터 재가공
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(permsRequestCode: Int, permissions: Array<String>, grandResults: IntArray) {
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
            }
            else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSIONS[1])) {
                    Toast.makeText(this, "권한이 거부되었습니다. 앱을 다시 실행하여 위치 권한을 허용해주세요.", Toast.LENGTH_LONG).show()
                    finish()
                }
                else {
                    Toast.makeText(this, "권한이 거부되었습니다. 설정(앱 정보)에서 위치 권한을 허용해야 합니다. ", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    private fun initAdapter() {
        viewPager.adapter = viewPagerAdapter
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val selectedHouseModel = viewPagerAdapter.currentList[position]
                val cameraUpdate = CameraUpdate.scrollTo(LatLng(selectedHouseModel.x!!.toDouble(), selectedHouseModel.y!!.toDouble()))
                    .animate(CameraAnimation.Easing)
                mMap?.moveCamera(cameraUpdate)
            }
        })
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initView() {

        val options = NaverMapOptions()
            .mapType(NaverMap.MapType.Basic)
            .enabledLayerGroups(NaverMap.LAYER_GROUP_BICYCLE)
            .compassEnabled(true)
            .scaleBarEnabled(true)

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
        currentLocationButton.map = naverMap

        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        getAPI()

    }


    private fun getAPI() {
        LLog.e("제휴병원 좌표")
        val query = ""
        val vercall: Call<MapMarker> = apiServices.getMap(prefs.newaccesstoken,query)
        vercall.enqueue(object : Callback<MapMarker> {
            override fun onResponse(call: Call<MapMarker>, response: Response<MapMarker>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"제휴병원 response SUCCESS -> $result")
                    result.let { dto ->
                        updateMarker(dto.result)
                        viewPagerAdapter.submitList(dto.result)
                        recyclerViewAdapter.submitList(dto.result)
                    }
                }
                else {
                    Log.d(LLog.TAG,"제휴병원 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<MapMarker>, t: Throwable) {
                Log.d(LLog.TAG, "제휴병원 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun updateMarker(result: List<AddressDocument>) {
        result.forEach { maps ->
            val marker = Marker()
            marker.position = LatLng(maps.x!!.toDouble(), maps.y!!.toDouble())
            marker.map = mMap
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = R.color.main_status
            marker.tag = maps.id
            marker.onClickListener = this
        }
    }


    fun onAddressClick(v: View) {
        searchAddress()
    }

    override fun onClick(overlay: Overlay): Boolean {
        viewPagerAdapter.currentList.firstOrNull { it.id == overlay.tag }
            ?.let {
                val position = viewPagerAdapter.currentList.indexOf(it)
                viewPager.currentItem = position
            }
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveMain()
        finishAffinity()
    }


}