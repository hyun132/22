package com.iium.iium_medioz.view.main.bottom.band

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentBandBinding
import com.iium.iium_medioz.databinding.FragmentMyBandBinding

class MyBandFragment : Fragment() {
    private lateinit var mBinding : FragmentMyBandBinding
    private lateinit var apiServices: APIService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_band, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        return mBinding.root
    }
}