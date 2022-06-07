package com.iium.iium_medioz.view.main.bottom.mypage.setting

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityProfileModifyBinding
import com.iium.iium_medioz.databinding.ActivitySettingBinding
import com.iium.iium_medioz.model.rest.login.GetUser
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.log.LLog
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ProfileModifyActivity : BaseActivity() {

    private lateinit var mBinding : ActivityProfileModifyBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_profile_modify)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
    }

    override fun onResume() {
        super.onResume()
        initAPI()
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

                    val nick_name = result.user?.name.toString()
                    val phone = result.user?.phone.toString()
                    mBinding.etNickname.text = Editable.Factory.getInstance().newEditable(nick_name)
                    mBinding.etPhone.text = Editable.Factory.getInstance().newEditable(phone)

                    val profile = result.user?.imgName
                    profileImg(profile)
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

                    val nick_name = result.user?.name.toString()
                    val phone = result.user?.phone.toString()
                    mBinding.etNickname.text = Editable.Factory.getInstance().newEditable(nick_name)
                    mBinding.etPhone.text = Editable.Factory.getInstance().newEditable(phone)

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
        LLog.e("프로필 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getProfileImg(profile, MyApplication.prefs.myaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"getProfileImg  response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            mBinding.profileProfileImg.setImageBitmap(bit)
                        }
                        catch (e: Exception) {
                            LLog.d(e.toString())
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"getProfileImg response ERROR -> $result")
                    otherImgAPI(profile)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "getProfileImg Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun otherImgAPI(profile: String?) {
        LLog.e("프로필 이미지_두번째 API")
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
                            mBinding.profileProfileImg.setImageBitmap(bit)
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

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    fun onBackPressed(v: View) {
        moveSetting()
    }

    fun onNickClick(v: View) {

    }

    fun onPhoneClick(v: View) {

    }

    fun onEmailClick(v: View) {

    }

    fun onWithdrawalClick(v: View) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveMain()
    }
}