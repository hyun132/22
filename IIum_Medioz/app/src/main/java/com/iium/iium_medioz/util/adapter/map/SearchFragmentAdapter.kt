package com.iium.iium_medioz.util.adapter.map

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.iium.iium_medioz.view.main.bottom.insurance.search.SearchFirstFragment
import com.iium.iium_medioz.view.main.bottom.insurance.search.SearchSecondFragment

private const val NUM_TABS = 2

class SearchFragmentAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return SearchFirstFragment()
            1 -> return SearchSecondFragment()
        }
        return SearchFirstFragment()
    }
}