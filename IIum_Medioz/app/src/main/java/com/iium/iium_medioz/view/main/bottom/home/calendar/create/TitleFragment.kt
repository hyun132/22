package com.iium.iium_medioz.view.main.bottom.home.calendar.create

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentFeelingBinding
import com.iium.iium_medioz.databinding.FragmentTitleBinding
import com.iium.iium_medioz.util.base.BaseFragment
import com.iium.iium_medioz.view.main.bottom.home.calendar.edit.CalendarEditActivity

class TitleFragment : BaseFragment() {

    private lateinit var mBinding : FragmentTitleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)
        mBinding.fragment = this
        initView()
        return mBinding.root
    }

    private fun initView() {
        val args by navArgs<TitleFragmentArgs>()

        mBinding.btnClose.setOnClickListener {
            requireActivity().finish()
        }

        mBinding.btnNext.setOnClickListener {
            Intent(requireActivity(), CalendarEditActivity::class.java).apply {
                putExtra("feeling", args.feeling)
                putExtra("title", mBinding.etTitle.text.toString())
                putExtra("date", args.date)
                startActivity(this)
                requireActivity().finish()
            }
        }    }

    fun onBackPressed(v: View) {
        findNavController().popBackStack()
    }

}