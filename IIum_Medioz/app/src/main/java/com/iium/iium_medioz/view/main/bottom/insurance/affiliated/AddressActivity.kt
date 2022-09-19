package com.iium.iium_medioz.view.main.bottom.insurance.affiliated


import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import com.iium.iium_medioz.databinding.ActivityAddressBinding
import com.iium.iium_medioz.model.map.AddressDocument
import com.iium.iium_medioz.model.map.MapMarker
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
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddressActivity : BaseActivity(), OnMapReadyCallback, Overlay.OnClickListener {

    private lateinit var mBinding : ActivityAddressBinding
    private lateinit var apiServices: APIService
    private var locationSource: FusedLocationSource? = null

    private var mMap: NaverMap?=null

    private val viewPager : ViewPager2 by lazy {
        findViewById(R.id.houseViewPager)
    }
    private val recyclerView : RecyclerView by lazy {
        findViewById(R.id.map_re)
    }

    private val recyclerViewAdapter = MapListAdapter(itemClickListener = {
        val intent = Intent(this, DocumentActivity::class.java)

        intent.apply {
            intent.putExtra(Constant.DOCUMENT_NAME, it.place_name.toString())
            intent.putExtra(Constant.DOCUMENT_ADDRESS, it.address_name)
            intent.putExtra(Constant.DOCUMENT_CALL, it.call.toString())
            intent.putExtra(Constant.DOCUMENT_IMGURL, it.imgURL.toString())
        }
        startActivity(intent)
    })

    private val viewPagerAdapter = MapViewPagerAdapter(itemClickListener = {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "[지금 이 가격에 예약하세요!!] ${it.address_name} ${it.place_name} 사진보기 : ${it.imgURL}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    })

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
            initAdapter()
        }
    }


    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveHospital()
        finishAffinity()
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
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
        }
        else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Constant.PERMISSIONS[0]
                )) {
                ActivityCompat.requestPermissions(this,
                    Constant.PERMISSIONS,
                    Constant.PERMISSION_REQUEST_CODE
                )
            }
            else {
                ActivityCompat.requestPermissions(this,
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
        if (permsRequestCode == Constant.PERMISSION_REQUEST_CODE && grandResults.size == Constant.PERMISSIONS.size) {
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
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Constant.PERMISSIONS[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Constant.PERMISSIONS[1])) {
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
//                val cameraUpdate = CameraUpdate.scrollTo(LatLng(selectedHouseModel.xvalue!!, selectedHouseModel.yvalue!!))
//                    .animate(CameraAnimation.Easing)
//                mMap?.moveCamera(cameraUpdate)
            }
        })
    }

    private fun initView() {

        val options = NaverMapOptions()
            .mapType(NaverMap.MapType.Basic)
            .enabledLayerGroups(NaverMap.LAYER_GROUP_BICYCLE)
            .compassEnabled(true)
            .scaleBarEnabled(true)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_address_fragment) as MapFragment?
            ?: MapFragment.newInstance(options).also {
                supportFragmentManager.beginTransaction().add(R.id.map_address_fragment, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.setIsMainNoticeViewed(false)
        finishAffinity()
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

//        val mapx = intent.getIntExtra(NAVER_MAPX,0)
//        val mapy = intent.getIntExtra(NAVER_MAPY,0)

        val kakao_mapx = intent.getStringExtra(KAKAO_MAPX)
        val kakap_mapy = intent.getStringExtra(KAKAO_MAPY)
        Log.d(TAG,"카메라 : ${kakao_mapx!!.toDouble()}, ${kakap_mapy!!.toDouble()}")

//        val tm = Tm128(mapx.toDouble(),mapy.toDouble())
//        Log.d(TAG,"카메라 카텍 변환 : $tm")
//        Log.d(TAG,"카메라 카텍 변환 lang : ${tm.toLatLng()}")

        val cameraUpdate: CameraUpdate = CameraUpdate.scrollTo(LatLng(kakao_mapx.toDouble(),kakap_mapy.toDouble()))
        naverMap.moveCamera(cameraUpdate)

        getAPI()
    }

    private fun getAPI() {
        LLog.e("제휴병원 좌표")
        val query = ""
        val vercall: Call<MapMarker> = apiServices.getMap(MyApplication.prefs.newaccesstoken,query)
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
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = R.color.main_status
            marker.tag = maps.id
            marker.onClickListener = this
        }
    }

    override fun onClick(overlay: Overlay): Boolean {
        viewPagerAdapter.currentList.firstOrNull { it.id == overlay.tag }
            ?.let {
                val position = viewPagerAdapter.currentList.indexOf(it)
                viewPager.currentItem = position
            }
        return true
    }



}