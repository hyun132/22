package com.iium.iium_medioz.view.main.bottom.mypage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentMyPageBinding
import com.iium.iium_medioz.model.rest.login.GetUser
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.view.main.banner.AchievementActivity
import com.iium.iium_medioz.view.main.bottom.mypage.cs.CsActivity
import com.iium.iium_medioz.view.main.bottom.mypage.notice.NoticeActivity
import com.iium.iium_medioz.view.main.bottom.mypage.setting.ProfileModifyActivity
import com.iium.iium_medioz.view.main.bottom.mypage.setting.SettingActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPageFragment : Fragment() {

    private lateinit var mBinding : FragmentMyPageBinding
    private lateinit var apiServices: APIService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page, container, false)
        apiServices = ApiUtils.apiService
        mBinding.fragment = this
        initAPI()
        return mBinding.root
    }

    private fun initAPI() {
        LLog.e("회원 정보 API")
        apiServices.getUser(MyApplication.prefs.myaccesstoken).enqueue(object :
            Callback<GetUser> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"GetUser API SUCCESS -> $result")
                    mBinding.tvMypageNickname.text = result.user?.name.toString()
                }
                else {
                    Log.d(LLog.TAG,"GetUser API ERROR -> ${response.errorBody()}")
                    otherAPI()
                }
            }

            override fun onFailure(call: Call<GetUser>, t: Throwable) {
                Log.d(LLog.TAG,"GetUser ERROR -> $t")

            }
        })
    }

    private fun otherAPI() {
        LLog.e("회원 정보_두번째 API")
        apiServices.getUser(MyApplication.prefs.newaccesstoken).enqueue(object :
            Callback<GetUser> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"GetUser Second API SUCCESS -> $result ")
                    val nickname = result.user?.name.toString()
                    mBinding.tvMypageNickname.text = "$nickname 님"
                }
                else {
                    Log.d(LLog.TAG,"GetUser Second API ERROR -> ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<GetUser>, t: Throwable) {
                Log.d(LLog.TAG,"GetUser Second ERROR -> $t")

            }
        })
    }

    fun onPointClick(v: View) {

    }

    fun onAchClick(v: View) {
        val intent = Intent(activity, AchievementActivity::class.java)
        startActivity(intent)
    }

    fun onCheckClick(v: View) {

    }

    fun onSettingClick(v: View) {
        val intent = Intent(activity, SettingActivity::class.java)
        startActivity(intent)
    }

    fun onProfileClick(v: View) {
        val intent = Intent(activity, ProfileModifyActivity::class.java)
        startActivity(intent)
    }

    fun onNotiClick(v: View) {
        val intent = Intent(activity, NoticeActivity::class.java)
        startActivity(intent)
    }

    fun onCsClick(v: View) {
        val intent = Intent(activity, CsActivity::class.java)
        startActivity(intent)
    }
}