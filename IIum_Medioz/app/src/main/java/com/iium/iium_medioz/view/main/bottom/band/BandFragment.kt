package com.iium.iium_medioz.view.main.bottom.band

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentBandBinding
import com.iium.iium_medioz.databinding.FragmentInsuranceBinding
import com.iium.iium_medioz.util.adapter.ViewPagerAdapter
import com.iium.iium_medioz.util.adapter.band.BandViewPagerAdapter
import com.iium.iium_medioz.view.main.bottom.band.upload.BandUploadActivity
import com.iium.iium_medioz.view.main.bottom.data.search.SearchActivity
import com.iium.iium_medioz.view.main.bottom.data.tablayout.AllFragment
import com.iium.iium_medioz.view.main.bottom.data.tablayout.SendFragment

class BandFragment : Fragment() {

    private lateinit var mBinding : FragmentBandBinding
    private lateinit var apiServices: APIService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_band, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this

        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val pagerAdapter = BandViewPagerAdapter(requireActivity())
        pagerAdapter.addFragment(AllBandFragment())
        pagerAdapter.addFragment(MyBandFragment())
        // Adapter
        mBinding.bandViewPager.adapter = pagerAdapter

        mBinding.bandViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position+1}")
            }
        })
        TabLayoutMediator(mBinding.bandTablayout, mBinding.bandViewPager) { tab, position ->
            val tabItem = arrayOf(
                "전체 글",
                "나의 글"
            )
            tab.text = tabItem[position]
        }.attach()
    }

    fun onSearchClick(v: View) {
        val intent = Intent(activity, BandUploadActivity::class.java)
        startActivity(intent)
    }

    fun onPlusClick(v: View) {
        val intent = Intent(activity, BandUploadActivity::class.java)
        startActivity(intent)
    }

}