package com.iium.iium_medioz.view.main.bottom.insurance.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.databinding.FragmentSearchFirstBinding


class SearchFirstFragment : Fragment() {

    private lateinit var mBinding : FragmentSearchFirstBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_first, container, false)
        mBinding.fragment = this
        return mBinding.root
    }

}

