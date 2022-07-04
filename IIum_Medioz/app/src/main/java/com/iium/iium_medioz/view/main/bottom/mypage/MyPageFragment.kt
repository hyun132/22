package com.iium.iium_medioz.view.main.bottom.mypage

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.FragmentMyPageBinding
import com.iium.iium_medioz.model.rest.login.GetUser
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import com.iium.iium_medioz.view.main.banner.AchievementActivity
import com.iium.iium_medioz.view.main.bottom.mypage.cs.CsActivity
import com.iium.iium_medioz.view.main.bottom.mypage.notice.NoticeActivity
import com.iium.iium_medioz.view.main.bottom.mypage.point.PointActivity
import com.iium.iium_medioz.view.main.bottom.mypage.setting.ProfileModifyActivity
import com.iium.iium_medioz.view.main.bottom.mypage.setting.SettingActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

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
        apiServices.getUser(prefs.myaccesstoken).enqueue(object :
            Callback<GetUser> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"GetUser API SUCCESS -> $result")
                    mBinding.tvMypageNickname.text = result.user?.name.toString()

                    val profile = result.user?.imgName
                    profileImg(profile)
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
                    Log.d(TAG,"GetUser Second API SUCCESS -> $result ")
                    val nickname = result.user?.name.toString()
                    mBinding.tvMypageNickname.text = "$nickname 님"
                    val profile = result.user?.imgName
                    profileImg(profile)
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

    private fun profileImg(profile: String?) {
        LLog.e("프로필 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getProfileImg(profile,prefs.myaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"getProfileImg  response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            mBinding.profileImg.setImageBitmap(bit)
                        }
                        catch (e: Exception) {
                            LLog.d(e.toString())
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"getProfileImg response ERROR -> $result")
                    otherImgAPI(profile)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "getProfileImg Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun otherImgAPI(profile: String?) {
        LLog.e("프로필 이미지_두번째 API")
        val vercall: Call<ResponseBody> = apiServices.getProfileImg(profile,prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"getProfileImg second response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            mBinding.profileImg.setImageBitmap(bit)
                        }
                        catch (e: Exception) {

                        }
                    }.start()

                }
                else {
                    Log.d(TAG,"getProfileImg second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "getProfileImg second Fail -> ${t.localizedMessage}")
            }
        })
    }

    fun onPointClick(v: View) {
        val intent = Intent(activity, PointActivity::class.java)
        startActivity(intent)

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