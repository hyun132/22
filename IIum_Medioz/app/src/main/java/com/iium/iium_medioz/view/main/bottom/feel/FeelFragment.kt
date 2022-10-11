package com.iium.iium_medioz.view.main.bottom.feel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.databinding.FragmentFeelBinding
import com.iium.iium_medioz.util.base.BaseFragment
import com.iium.iium_medioz.util.dialog.LoadingDialog
import com.iium.iium_medioz.view.main.bottom.home.calendar.CalendarActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class FeelFragment : Fragment() {

    private lateinit var mBinding : FragmentFeelBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feel, container, false)
        mBinding.fragment = this
        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        initView()
    }

    private fun initView() {
        val intent = Intent(activity, TestCalendarActivity::class.java)
        startActivity(intent)
    }
}