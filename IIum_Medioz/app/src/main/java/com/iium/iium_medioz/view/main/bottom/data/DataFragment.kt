package com.iium.iium_medioz.view.main.bottom.data

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentDataBinding
import com.iium.iium_medioz.util.adapter.ViewPagerAdapter
import com.iium.iium_medioz.view.main.banner.AchievementActivity
import com.iium.iium_medioz.view.main.bottom.data.search.SearchActivity
import com.iium.iium_medioz.view.main.bottom.data.tablayout.AllFragment
import com.iium.iium_medioz.view.main.bottom.data.tablayout.SendFragment

class DataFragment : Fragment() {

    private lateinit var mBinding : FragmentDataBinding
    private lateinit var apiServices: APIService
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_data, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        return mBinding.root
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val pagerAdapter = ViewPagerAdapter(requireActivity())
        pagerAdapter.addFragment(AllFragment())
        pagerAdapter.addFragment(SendFragment())
        // Adapter
        mBinding.dataViewPager.adapter = pagerAdapter

        mBinding.dataViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                Log.e("ViewPagerFragment", "Page ${position+1}")
            }
        })
        TabLayoutMediator(mBinding.dataTablayout, mBinding.dataViewPager) { tab, position ->
            val tabItem = arrayOf(
                "전체",
                "판매"
            )
            tab.text = tabItem[position]
        }.attach()
    }

    fun onSearchClick(v: View) {
        val intent = Intent(activity, SearchActivity::class.java)
        startActivity(intent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(this@DataFragment)
                    ?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()

    }
}