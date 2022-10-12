package com.iium.iium_medioz.view.main.bottom.feel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.iium.iium_medioz.R
import com.iium.iium_medioz.databinding.FragmentFeelBinding
import com.iium.iium_medioz.util.calendar.isSameDay
import com.iium.iium_medioz.util.common.AutoClearedValue
import com.iium.iium_medioz.util.dialog.DateDialog
import com.iium.iium_medioz.util.log.debugE
import com.iium.iium_medioz.viewmodel.calendar.CalendarViewModel
import com.iium.iium_medioz.viewmodel.calendar.FeelViewModel
import com.iium.iium_medioz.viewmodel.main.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate


@FlowPreview
@AndroidEntryPoint
class FeelFragment : Fragment(), DateDialog.OnClickListener {

    private var mBinding by AutoClearedValue<FragmentFeelBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentFeelBinding.inflate(layoutInflater, container, false).also { mBinding = it }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        initView()
    }

    override fun onClick(date: LocalDate) {
        mBinding.calendarView.selectedDate = date
    }

    private fun initView() {
        val intent = Intent(activity, TestCalendarActivity::class.java)
        startActivity(intent)
    }


}