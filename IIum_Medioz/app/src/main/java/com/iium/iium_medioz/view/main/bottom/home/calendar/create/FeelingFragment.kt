package com.iium.iium_medioz.view.main.bottom.home.calendar.create

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentFeelingBinding
import com.iium.iium_medioz.databinding.FragmentHomeBinding
import com.iium.iium_medioz.view.main.MainActivity
import com.iium.iium_medioz.view.main.banner.GuideActivity
import java.text.SimpleDateFormat
import java.util.*

class FeelingFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var mBinding : FragmentFeelingBinding
    private lateinit var apiServices: APIService
    private val formatter = SimpleDateFormat("yyyy년 MMM dd일, EEE요일")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feeling, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        initView()
        return mBinding.root
    }

    private fun initView() {
        val date = Date()
        if (requireActivity().intent.hasExtra("date")) {
            mBinding.currentDate.text = requireActivity().intent.getStringExtra("date")
        } else {
            mBinding.currentDate.text = formatter.format(date)
        }

        var feeling = 1

        val emotes = listOf(
            mBinding.llAngry,
            mBinding.llSad,
            mBinding.llHappy,
            mBinding.llThanks,
            mBinding.llSurprise
        )
        emotes.forEachIndexed { index, lyt ->
            lyt.setOnClickListener {
                emotes.forEach { view -> view.unselect() }
                lyt.select()
                feeling = index
            }
        }

        mBinding.btnClose.setOnClickListener {
            requireActivity().finish()
        }

        mBinding.btnNext.setOnClickListener {
            val action = FeelingFragmentDirections.actionFeelingFragmentToTitleFragment(
                feeling,
                mBinding.currentDate.text.toString()
            )
            findNavController().navigate(action)
        }

        mutableListOf(mBinding.btnIconCalendar, mBinding.currentDate)
            .forEach {
                it.setOnClickListener {
                    val c = Calendar.getInstance()
                    val year = c.get(Calendar.YEAR)
                    val month = c.get(Calendar.MONTH)
                    val day = c.get(Calendar.DAY_OF_MONTH)

                    DatePickerDialog(requireActivity(), this, year, month, day)
                        .show()
                }
            }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val mCalendar = Calendar.getInstance()
        mCalendar[Calendar.YEAR] = year
        mCalendar[Calendar.MONTH] = month
        mCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
        mBinding.currentDate.text = formatter.format(mCalendar.time)
    }

    fun onBackPressed(v: View) {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)

    }
}

fun View.select() {
    setBackgroundResource(R.drawable.bg_color_merahmuda)
}

fun View.unselect() {
    setBackgroundResource(0)
}