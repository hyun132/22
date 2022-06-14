package com.iium.iium_medioz.view.main.bottom.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.graphics.get
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityDataDetyailBinding
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.DATA_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.DATA_NORMAL
import com.iium.iium_medioz.util.`object`.Constant.DATA_TEXTIMG
import com.iium.iium_medioz.util.`object`.Constant.DATA_TIMESTAMP
import com.iium.iium_medioz.util.`object`.Constant.DATA_TITLE
import com.iium.iium_medioz.util.`object`.Constant.DATA_VIDEOFILE
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.util.log.LLog.TAG
import okhttp3.ResponseBody
import okio.utf8Size
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataDetyailActivity : BaseActivity() {

    private lateinit var mBinding : ActivityDataDetyailBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_detyail)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        initView()
        inStatusBar()
    }

    private fun initView() {
        val title = intent.getStringExtra(DATA_TITLE)
        val keyword = intent.getStringExtra(DATA_KEYWORD)
        val timestamp = intent.getStringExtra(DATA_TIMESTAMP)
        val textList = intent.getStringExtra(DATA_TEXTIMG)
        val normalList = intent.getStringExtra(DATA_NORMAL)
        val videoList = intent.getStringExtra(DATA_VIDEOFILE)
        Log.d(TAG,"일반 이미지 -> $normalList")

        val img =  textList?.substring(2)
        val imgtest = img?.substring(0, img.length - 2)
        val tnla = imgtest?.split(",")

        for(i in 0 until tnla?.size!! step(1)) {
            val str_idx = i.toString()
            Thread {
                try {
                    when (str_idx) {
                        "0" -> first(tnla[i].trim())
                        "1" -> second(tnla[i].trim())
                        "2" -> third(tnla[i].trim())
                        "3" -> four(tnla[i].trim())
                        "4" -> five(tnla[i].trim())
                        else -> Log.d(TAG, "실패")
                    }
                }
                catch (e: Exception) {
                    LLog.d(e.toString())
                }
            }.start()
        }

        val normal =  normalList?.substring(2)
        val normaltest = normal?.substring(0, normal.length - 2)
        val normalstart = normaltest?.split(",")

        for(i in 0 until normalstart?.size!! step(1)) {
            val str_idx = i.toString()
            Thread {
                try {
                    when (str_idx) {
                        "0" -> normal_first(normalstart[i].trim())
                        "1" -> normal_second(normalstart[i].trim())
                        "2" -> normal_third(normalstart[i].trim())
                        "3" -> normal_four(normalstart[i].trim())
                        "4" -> normal_five(normalstart[i].trim())
                        else -> Log.d(TAG, "실패")
                    }
                }
                catch (e: Exception) {
                    LLog.d(e.toString())
                }
            }
        }

//        val video =  videoList?.substring(2)
//        val videotest = video?.substring(0, img.length - 2)
//        val videostart = videotest?.split(",")
//
//        for(i in 0 until videostart?.size!! step(1)) {
//            val str_idx = i.toString()
//            when (str_idx) {
//                "0" -> video_first(videostart[i].trim())
//                "1" -> video_second(videostart[i].trim())
//                "2" -> video_third(videostart[i].trim())
//                else -> Log.d(TAG,"실패")
//            }
//        }


        mBinding.tvMedicalDetailTitle.text = title.toString()
        mBinding.tvMedicalDetailData.text = timestamp.toString()
        mBinding.tvMyKeyword.text = keyword.toString()

    }

    //////////////////////텍스트 이미지 추출 API////////////////////////////
    private fun first(textFirst: String) {
        LLog.e("텍스트 첫번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(textFirst,prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"텍스트 첫번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image1.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(TAG,"텍스트 첫번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"텍스트 첫번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "텍스트 첫번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun second(second: String) {
        LLog.e("텍스트 두번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(second,prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"텍스트 두번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image2.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(TAG,"텍스트 두번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"텍스트 두번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "텍스트 두번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun third(third: String) {
        LLog.e("텍스트 세번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(third,prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"텍스트 세번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image3.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(TAG,"텍스트 세번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"텍스트 세번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "텍스트 세번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun four(four: String) {
        LLog.e("텍스트 네번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(four,prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"텍스트 네번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image4.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(TAG,"텍스트 네번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"텍스트 네번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "텍스트 네번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun five(five: String) {
        LLog.e("텍스트 다섯번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(five,prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"텍스트 다섯번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image5.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(TAG,"텍스트 다섯번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"텍스트 다섯번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "텍스트 다섯번째 Fail -> ${t.localizedMessage}")
            }
        })
    }


    //////////////////////일반 이미지 추출 API////////////////////////////
    private fun normal_first(s: String) {
        LLog.e("일반 첫번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(s,prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"일반 첫번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage1.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(TAG,"일반 첫번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"일반 첫번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "일반 첫번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun normal_second(s: String) {
        LLog.e("일반 두번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(s,prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"일반 두번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage2.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(TAG,"일반 두번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"일반 두번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "일반 두번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun normal_third(s: String) {
        LLog.e("일반 세번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(s,prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"일반 세번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage3.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(TAG,"일반 세번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"일반 세번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "일반 세번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun normal_four(s: String) {
        LLog.e("일반 네번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(s,prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"일반 네번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage4.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(TAG,"일반 네번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"일반 네번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "일반 네번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun normal_five(s: String) {
        LLog.e("일반 다섯번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(s,prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(TAG,"일반 다섯번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage5.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(TAG,"일반 다섯번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(TAG,"일반 다섯번째  response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "일반 다섯번째 Fail -> ${t.localizedMessage}")
            }
        })
    }


    //////////////////////영상 추출 API////////////////////////////
    private fun video_first(s: String) {

    }

    private fun video_second(s: String) {

    }

    private fun video_third(s: String) {

    }


    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.main_status)
    }

    fun onBackPressed(v: View?){
        moveMain()
    }

    fun onDataSend(v: View?) {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveMain()
    }

}