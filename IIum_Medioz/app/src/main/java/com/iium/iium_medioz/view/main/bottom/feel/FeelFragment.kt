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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FeelFragment : BaseFragment() {

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
        initView()
    }

    private fun initView() {
//        val dialog = context?.let {
//            LoadingDialog(it)
//        }
//        CoroutineScope(Main).launch {
//            dialog?.show()
//            delay(2000)
//            val intent = Intent(activity, CalendarActivity::class.java)
//            startActivity(intent)
//            dialog?.dismiss()
//        }
//    }
        val intent = Intent(activity, CalendarActivity::class.java)
        startActivity(intent)
    }
}