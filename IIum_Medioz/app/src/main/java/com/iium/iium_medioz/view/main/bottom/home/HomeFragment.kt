package com.iium.iium_medioz.view.main.bottom.home

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentHomeBinding
import com.iium.iium_medioz.model.rest.login.GetUser
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import com.iium.iium_medioz.view.main.banner.AchievementActivity
import com.iium.iium_medioz.view.main.banner.GuideActivity
import com.iium.iium_medioz.view.main.bottom.home.calendar.CalendarActivity
import com.iium.iium_medioz.view.main.bottom.insurance.affiliated.HospitalActivity
import kotlinx.android.synthetic.main.view_photo_item.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var mBinding : FragmentHomeBinding
    private lateinit var apiServices: APIService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        initView()
        initAPI()
        return mBinding.root
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
        LLog.e("회원 정보 API")
        apiServices.getUser(prefs.myaccesstoken).enqueue(object :
            Callback<GetUser> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"GetUser API SUCCESS -> $result")
                    mBinding.tvNickname.text = result.user?.name.toString()
                }
                else {
                    Log.d(TAG,"GetUser API ERROR -> ${response.errorBody()}")
                    otherAPI()
                }
            }

            override fun onFailure(call: Call<GetUser>, t: Throwable) {
                Log.d(TAG,"GetUser ERROR -> $t")

            }
        })
    }

    private fun otherAPI() {
        LLog.e("회원 정보_두번째 API")
        apiServices.getUser(prefs.newaccesstoken).enqueue(object :
            Callback<GetUser> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"GetUser Second API SUCCESS -> $result")
                    mBinding.tvNickname.text = result.user?.name.toString()
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

}