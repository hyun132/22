package com.iium.iium_medioz.view.main.bottom.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import io.realm.Realm
import kotlinx.android.synthetic.main.one_button_dialog.view.*
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.thread


class HomeFragment : BaseFragment(), LifecycleObserver {

    private lateinit var mBinding : FragmentHomeBinding
//    private lateinit var apiServices: APIService
    private lateinit var callback: OnBackPressedCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
//        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        initView()
//        initTemperature()
        return mBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAPI()
    }

    private fun initView() {            // 메인 상단 Background 변경
        val images = intArrayOf(
            R.drawable.main_first,
            R.drawable.main_second,
            R.drawable.main_third,
            R.drawable.main_banner_four
        )
        val imageId = (Math.random() * images.size).toInt()
        mBinding.clMain.setBackgroundResource(images[imageId])
    }

    private fun initAPI() {
        LLog.e("회원 정보_두번째 API")
        apiServices.getUser(prefs.newaccesstoken).enqueue(object :
            Callback<GetUser> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"GetUser Second API SUCCESS -> $result")
                    if (response.code() == 404 || response.code() == 400 || response.code() == 401) {
                        return
                    } else {
                        mBinding.tvNickname.text = result.user?.name.toString()
                        initList()
                    }

                }
                else {
                    Log.d(TAG,"GetUser Second API ERROR -> ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<GetUser>, t: Throwable) {
                Log.d(TAG,"GetUser Second ERROR -> $t")

            }
        })
    }

    private fun initList() {
        LLog.e("데이터 조회_두번째 API")
        val vercall: Call<MedicalData> = apiServices.getCreateGet(prefs.newaccesstoken)
        vercall.enqueue(object : Callback<MedicalData> {
            override fun onResponse(call: Call<MedicalData>, response: Response<MedicalData>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    if (response.code() == 404 || response.code() == 400 || response.code() == 401) {
                        return initList()
                    } else {
                        val allscore = result.datalist?.map { it.allscore }
                        if(allscore?.size!! > 0) {
                            initData(allscore)
                        } else {
                            Log.d(TAG,"데이터가 없습니다.")
                        }
                    }
                }
                else {
                    Log.d(TAG,"메인 데이터 조회 에러 -> ${response.errorBody()}")
                }
            }
            override fun onFailure(call: Call<MedicalData>, t: Throwable) {
                Log.d(TAG, "List Second Fail -> $t")
            }
        })
    }

    private fun initData(allscore: List<String?>?) {
        val all : List<Int>? = allscore?.map { it!!.toInt() }
        val allitem = all?.reduce { result, item -> result + item  }
        val allsize = all?.count()
        val all_score = allitem!! / allsize!!

        mBinding.tvTem.text = all_score.toString()

        when {
            (all_score in 0..19) -> mBinding.imgDataTem.setImageResource(R.drawable.main_tem_first)
            (all_score in 20..39) -> mBinding.imgDataTem.setImageResource(R.drawable.main_tem_second)
            (all_score in 40..59) -> mBinding.imgDataTem.setImageResource(R.drawable.main_tem_third)
            (all_score in 60..79) -> mBinding.imgDataTem.setImageResource(R.drawable.main_tem_four)
            (all_score in 80..100) -> mBinding.imgDataTem.setImageResource(R.drawable.main_tem_five)
            else -> mBinding.imgDataTem.setImageResource(R.drawable.main_tem_first)
        }

        when {
            (allsize in 0..9) -> mBinding.tvLabel.text = "1"
            (allsize in 10..19) -> mBinding.tvLabel.text = "2"
            (allsize in 20..29) -> mBinding.tvLabel.text = "3"
            (allsize in 30..39) -> mBinding.tvLabel.text = "4"
            (allsize in 40..49) -> mBinding.tvLabel.text = "5"
            (allsize in 50..59) -> mBinding.tvLabel.text = "6"
            (allsize in 60..69) -> mBinding.tvLabel.text = "7"
            (allsize in 70..79) -> mBinding.tvLabel.text = "8"
            (allsize in 80..89) -> mBinding.tvLabel.text = "9"
            (allsize in 90..99) -> mBinding.tvLabel.text = "10"
            (allsize in 100..109) -> mBinding.tvLabel.text = "11"
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