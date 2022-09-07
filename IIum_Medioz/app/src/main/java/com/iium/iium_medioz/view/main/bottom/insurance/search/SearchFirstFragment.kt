package com.iium.iium_medioz.view.main.bottom.insurance.search

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.iium.iium_medioz.R
import com.iium.iium_medioz.databinding.FragmentSearchFirstBinding
import com.iium.iium_medioz.util.adapter.map.KaKaoLocalAdapter
import com.iium.iium_medioz.viewmodel.map.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel


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

