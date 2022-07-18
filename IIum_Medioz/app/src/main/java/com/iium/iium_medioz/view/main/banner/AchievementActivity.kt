package com.iium.iium_medioz.view.main.banner

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
import com.iium.iium_medioz.databinding.ActivityAchievementBinding
import com.iium.iium_medioz.databinding.ActivityGuideBinding
import com.iium.iium_medioz.model.calendar.CalendarModel
import com.iium.iium_medioz.model.document.DocumentListModel
import com.iium.iium_medioz.model.recycler.MedicalData
import com.iium.iium_medioz.model.rest.login.GetUser
import com.iium.iium_medioz.model.send.SendTestModel
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.common.CommonData
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_achievement.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AchievementActivity : BaseActivity() {
    private lateinit var mBinding : ActivityAchievementBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_achievement)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
        initAPI()
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status )
    }

    private fun initAPI() {
        initUser()
        initData()
        initcalendar()
        initSendData()
        initDocument()

    }

    private fun initUser() {
        LLog.e("회원 정보 API")
        apiServices.getUser(prefs.myaccesstoken).enqueue(object :
            Callback<GetUser> {
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"GetUser API SUCCESS -> $result")
                    mBinding.tvAchNickname.text = result.user?.name.toString()
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
        apiServices.getUser(prefs.newaccesstoken).enqueue(object :
            Callback<GetUser> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<GetUser>, response: Response<GetUser>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"GetUser Second API SUCCESS -> $result ")
                    val nickname = result.user?.name.toString()
                    mBinding.tvAchNickname.text = "$nickname 님"
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
        val vercall: Call<ResponseBody> = apiServices.getProfileImg(profile,prefs.myaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"getProfileImg  response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            mBinding.achProfileImg.setImageBitmap(bit)
                        }
                        catch (e: Exception) {

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
        val vercall: Call<ResponseBody> = apiServices.getProfileImg(profile, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"getProfileImg second response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            mBinding.achProfileImg.setImageBitmap(bit)
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

    private fun initData() {
        LLog.e("나의 의료데이터 조회 API")
        apiServices.getCreateGet(prefs.newaccesstoken).enqueue(object :
            Callback<MedicalData> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<MedicalData>, response: Response<MedicalData>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"나의 의료데이터 조회 SUCCESS -> $result ")
                    val calendarSize = result.datalist?.size
                    when {
                        (calendarSize in 1..1000)-> mBinding.ach1.setImageResource(R.drawable.ach_1)
                        (calendarSize in 10..1000) -> mBinding.ach5.setImageResource(R.drawable.ach_5)
                        (calendarSize in 20..1000) -> mBinding.ach6.setImageResource(R.drawable.ach_6)
                        (calendarSize in 30..1000) -> mBinding.ach7.setImageResource(R.drawable.ach_7)
                        else ->{

                        }
                    }

                }
                else {
                    Log.d(LLog.TAG,"나의 의료데이터 조회ERROR -> ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<MedicalData>, t: Throwable) {
                Log.d(LLog.TAG,"나의 의료데이터 조회 ERROR -> $t")

            }
        })
    }

    private fun initcalendar() {
        LLog.e("캘린더 조회 API")
        apiServices.getFeel(prefs.newaccesstoken).enqueue(object :
            Callback<CalendarModel> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<CalendarModel>, response: Response<CalendarModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"캘린더 조회 SUCCESS -> $result ")
                    val calendarSize = result.calendarList.size
                    if(calendarSize in 30..1000) {
                        mBinding.ach3.setImageResource(R.drawable.ach_3)
                    } else {
                        mBinding.ach3.setImageResource(R.drawable.bch_3)
                    }

                }
                else {
                    Log.d(LLog.TAG,"캘린더 조회 ERROR -> ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<CalendarModel>, t: Throwable) {
                Log.d(LLog.TAG,"캘린더 조회 ERROR -> $t")

            }
        })
    }

    private fun initSendData() {
        LLog.e("의료데이터 판매 조회 API")
        apiServices.getSend(prefs.newaccesstoken).enqueue(object :
            Callback<SendTestModel> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<SendTestModel>, response: Response<SendTestModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"의료데이터 판매 조회 SUCCESS -> $result ")
                    val calendarSize = result.datalist?.size
                    if(calendarSize in 10..1000) {
                        mBinding.ach3.setImageResource(R.drawable.ach_2)
                    } else {
                        mBinding.ach3.setImageResource(R.drawable.bch_2)
                    }

                }
                else {
                    Log.d(LLog.TAG,"의료데이터 판매 조회 ERROR -> ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<SendTestModel>, t: Throwable) {
                Log.d(LLog.TAG,"의료데이터 판매 조회 ERROR -> $t")

            }
        })
    }

    private fun initDocument() {
        LLog.e("제휴병원 서류 조회 API")
        apiServices.getDocument(prefs.newaccesstoken).enqueue(object :
            Callback<DocumentListModel> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<DocumentListModel>, response: Response<DocumentListModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(LLog.TAG,"제휴병원 서류 조회 SUCCESS -> $result ")
                    val calendarSize = result.documentList.size
                    if(calendarSize in 10..1000) {
                        mBinding.ach3.setImageResource(R.drawable.ach_4)
                    } else {
                        mBinding.ach3.setImageResource(R.drawable.bch_4)
                    }

                }
                else {
                    Log.d(LLog.TAG,"제휴병원 서류 조회 ERROR -> ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<DocumentListModel>, t: Throwable) {
                Log.d(LLog.TAG,"제휴병원 서류 조회 ERROR -> $t")

            }
        })
    }

    fun onBackPressed(v: View) {
        moveMain()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}