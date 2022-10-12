package com.iium.iium_medioz.view.main.bottom.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentHomeBinding
import com.iium.iium_medioz.model.recycler.MedicalData
import com.iium.iium_medioz.model.rest.base.AppPolicy
import com.iium.iium_medioz.model.rest.base.Policy
import com.iium.iium_medioz.model.rest.login.GetUser
import com.iium.iium_medioz.model.rest.login.PutUser
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.base.BaseFragment
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.feel.dp
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import com.iium.iium_medioz.util.popup.ImageNoticePopup
import com.iium.iium_medioz.view.main.banner.AchievementActivity
import com.iium.iium_medioz.view.main.banner.GuideActivity
import com.iium.iium_medioz.view.main.bottom.home.calendar.CalendarActivity
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.HospitalActivity
import com.iium.iium_medioz.view.main.bottom.mypage.MyPageActivity
import com.iium.iium_medioz.view.term.SecondDialog
import com.iium.iium_medioz.viewmodel.main.home.HomeViewModel
import io.realm.Realm
import kotlinx.android.synthetic.main.one_button_dialog.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread
import kotlin.system.exitProcess

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class HomeFragment : BaseFragment(), LifecycleObserver {

    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var mBinding : FragmentHomeBinding
    //    private lateinit var apiServices: APIService
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
//        apiServices = ApiUtils.apiService
        mBinding.apply {
            fragment = this@HomeFragment
            lifecycleOwner = viewLifecycleOwner
            viewModel = homeViewModel
        }
//        initTemperature()
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.mutableErrorMessage.observe(viewLifecycleOwner){
            LLog.e("HomeFragment : onCreate${it}")
            ErrorDialog()
        }
    }

    fun onGuideClick(v: View?) {
        val intent = Intent(activity, GuideActivity::class.java)
        startActivity(intent)

    }

    fun onAchClick(v: View?) {
        val intent = Intent(activity, AchievementActivity::class.java)
        startActivity(intent)

    }

    fun onHosClick(v: View?) {
        val intent = Intent(activity, HospitalActivity::class.java)
        startActivity(intent)
    }

    fun onFeelingClick(v: View?) {
        val intent = Intent(activity, CalendarActivity::class.java)
        startActivity(intent)
    }

    fun onMyPage(v: View) {
        val intent = Intent(activity, MyPageActivity::class.java)
        startActivity(intent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(this@HomeFragment)
                    ?.commit()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        activity?.lifecycle?.addObserver(this@HomeFragment)
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()

    }
}