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
import com.iium.iium_medioz.util.base.BaseFragment
import com.iium.iium_medioz.util.extensions.onTextChanged
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.AddressActivity
import com.iium.iium_medioz.viewmodel.map.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.ObsoleteCoroutinesApi


class SearchFirstFragment : BaseFragment() {

    private lateinit var mBinding : FragmentSearchFirstBinding
    private var readapter: HospitalAdapter?=null
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val viewModel: SearchViewModel by viewModels()
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private var mFusedLocationProviderClient: FusedLocationProviderClient? =
        null
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_first, container, false)
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

    private fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
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
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation
            onLocationChanged(locationResult.lastLocation!!)
        }
    }

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

    fun onLocationChanged(location: Location) {
        mLastLocation = location
        latitude = mLastLocation.latitude
        longitude = mLastLocation.longitude
        println("위도 : " + mLastLocation.latitude)
        println("경도 : " + mLastLocation.longitude)
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
        mBinding.rvResult.adapter = readapter
        mBinding.rvResult.setHasFixedSize(true)
        mBinding.rvResult.layoutManager = LinearLayoutManager(context)
        readapter!!.setOnItemClickListener { x, y ->
            val intent = Intent(activity, AddressActivity::class.java)
            intent.putExtra(KAKAO_MAPX, x)
            intent.putExtra(KAKAO_MAPY, y)
            startActivity(intent)
        }

        mBinding.textField.onTextChanged { s, start, before, count ->
            val queryText = s.toString()
            readapter!!.submitVariable(queryText, latitude, longitude)
            viewModel.queryChannel.trySend(queryText).isSuccess
        }
    }
}

