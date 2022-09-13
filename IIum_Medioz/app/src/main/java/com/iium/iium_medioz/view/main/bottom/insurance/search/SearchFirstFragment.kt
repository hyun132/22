package com.iium.iium_medioz.view.main.bottom.insurance.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentSearchFirstBinding
import com.iium.iium_medioz.util.`object`.Constant.KAKAO_MAPX
import com.iium.iium_medioz.util.`object`.Constant.KAKAO_MAPY
import com.iium.iium_medioz.util.adapter.map.HospitalAdapter
import com.iium.iium_medioz.util.extensions.onTextChanged
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.AddressActivity
import com.iium.iium_medioz.viewmodel.map.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi


class SearchFirstFragment : Fragment() {

    private lateinit var mBinding : FragmentSearchFirstBinding
    private lateinit var apiServices: APIService
    private var readapter: HospitalAdapter?=null
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val viewModel: SearchViewModel by viewModels()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private var mFusedLocationProviderClient: FusedLocationProviderClient? =
        null // 현재 위치를 가져오기 위한 변수
    lateinit var mLastLocation: Location // 위치 값을 가지고 있는 객체
    internal lateinit var mLocationRequest: LocationRequest // 위치 정보 요청의 매개변수를 저장하는
    private val REQUEST_PERMISSION_LOCATION = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_first, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        initView()
        init()
        initlocation()
        return mBinding.root
    }

    private fun initlocation() {
        mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (checkPermissionForLocation(context!!)) {
            startLocationUpdates()
        }
    }

    // 위치 권한이 있는지 확인하는 메서드
    private fun checkPermissionForLocation(context: Context): Boolean {
        // Android 6.0 Marshmallow 이상에서는 위치 권한에 추가 런타임 권한이 필요
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                // 권한이 없으므로 권한 요청 알림 보내기
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }

    private fun startLocationUpdates() {

        //FusedLocationProviderClient의 인스턴스를 생성.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity!!)
        if (ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        // 기기의 위치에 관한 정기 업데이트를 요청하는 메서드 실행
        // 지정한 루퍼 스레드(Looper.myLooper())에서 콜백(mLocationCallback)으로 위치 업데이트를 요청
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    // 시스템으로 부터 위치 정보를 콜백으로 받음
    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            // 시스템에서 받은 location 정보를 onLocationChanged()에 전달
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation!!)
        }
    }

    // 사용자에게 권한 요청 후 결과에 대한 처리 로직
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()

            } else {
                Log.d("ttt", "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(context, "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 시스템으로 부터 받은 위치정보를 화면에 갱신해주는 메소드
    fun onLocationChanged(location: Location) {
        mLastLocation = location
        latitude = mLastLocation.latitude
        longitude = mLastLocation.longitude
        println("위도 : " + mLastLocation.latitude) // 갱신 된 위도
        println("경도 : " + mLastLocation.longitude) // 갱신 된 경도
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    @SuppressLint("FragmentLiveDataObserve")
    private fun initView() {
        viewModel.searchResult.observe(this, Observer {
        })
        viewModel.status.observe(this, Observer {
            if (it) {
                if (viewModel.kakaoList.value!!.result.isEmpty()) {
                    mBinding.clSearchAddress.visibility = View.GONE
                    mBinding.tvNoData.visibility = View.VISIBLE
                } else {
                    readapter?.submitList(viewModel.kakaoList.value!!.result)
                    if (mBinding.clSearchAddress.visibility == View.GONE) {
                        mBinding.clSearchAddress.visibility = View.VISIBLE
                    }
                    if (mBinding.tvNoData.visibility == View.VISIBLE) {
                        mBinding.tvNoData.visibility = View.GONE
                    }
                }
            } else {
                if (mBinding.clSearchAddress.visibility == View.VISIBLE) {
                    mBinding.clSearchAddress.visibility = View.GONE
                }
                if (mBinding.tvNoData.visibility == View.GONE) {
                    mBinding.tvNoData.visibility = View.VISIBLE
                }
            }
        })
    }

    @OptIn(ObsoleteCoroutinesApi::class, ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun init() {
        readapter = HospitalAdapter()
        // RecyclerView 설정
        mBinding.rvResult.adapter = readapter
        mBinding.rvResult.setHasFixedSize(true)
        mBinding.rvResult.layoutManager = LinearLayoutManager(context)
        readapter!!.setOnItemClickListener { x, y ->
            val intent = Intent(activity, AddressActivity::class.java)
            intent.putExtra(KAKAO_MAPX, x)
            intent.putExtra(KAKAO_MAPY, y)
            startActivity(intent)
        }

        // EditText 입력 값에 변화가 있으면 BroadcastChannel로 값 전송
        mBinding.textField.onTextChanged { s, start, before, count ->
            val queryText = s.toString()
            readapter!!.submitVariable(queryText, latitude, longitude)
            // Channel에 queryText 전송, Channel 용량을 침범하지 않았다면 true 아니면 false 리턴
            viewModel.queryChannel.trySend(queryText).isSuccess
        }
    }
}

