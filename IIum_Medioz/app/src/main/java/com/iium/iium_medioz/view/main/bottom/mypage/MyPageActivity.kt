package com.iium.iium_medioz.view.main.bottom.mypage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityMainBinding
import com.iium.iium_medioz.databinding.ActivityMyPageBinding
import com.iium.iium_medioz.model.rest.login.GetUser
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.log.LLog
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
import java.lang.IllegalArgumentException

class MyPageActivity : BaseActivity() {
    private lateinit var mBinding : ActivityMyPageBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_page)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        runOnUiThread {
            initAPI()
        }
        inStatusBar()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initAPI() {
        LLog.e("?????? ??????_????????? API")
        apiServices.getUser(MyApplication.prefs.newaccesstoken).enqueue(object :
            Callback<GetUser> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"GetUser Second API SUCCESS -> $result ")
                    val nickname = result.user?.name.toString()
                    mBinding.tvMypageNickname.text = "$nickname ???"
                    val profile = result.user?.imgName
                    profileImg(profile)
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

    private fun profileImg(profile: String?) {
        LLog.e("????????? ?????????_????????? API")
        val vercall: Call<ResponseBody> = apiServices.getProfileImg(profile, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"getProfileImg second response SUCCESS -> $result")
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
                    Log.d(LLog.TAG,"getProfileImg second response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "getProfileImg second Fail -> ${t.localizedMessage}")
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveMain()
        finishAffinity()
    }

    fun onPointClick(v: View) {
        val intent = Intent(this, PointActivity::class.java)
        startActivity(intent)

    }

    fun onAchClick(v: View) {
        val intent = Intent(this, AchievementActivity::class.java)
        startActivity(intent)
    }

    fun onCheckClick(v: View) {

    }

    fun onSettingClick(v: View) {
        val intent = Intent(this, SettingActivity::class.java)
        startActivity(intent)
    }

    fun onProfileClick(v: View) {
        val intent = Intent(this, ProfileModifyActivity::class.java)
        startActivity(intent)
    }

    fun onNotiClick(v: View) {
        val intent = Intent(this, NoticeActivity::class.java)
        startActivity(intent)
    }

    fun onCsClick(v: View) {
        val intent = Intent(this, CsActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        try {

        } catch (e: IllegalArgumentException) {
            Log.d(TAG,e.toString())
        }
    }

}