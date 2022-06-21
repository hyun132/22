package com.iium.iium_medioz.view.main.bottom.data.send

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySendBinding
import com.iium.iium_medioz.model.send.*
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.DEFAULT_CODE_FALSE
import com.iium.iium_medioz.util.`object`.Constant.OCR_SECRET
import com.iium.iium_medioz.util.`object`.Constant.OCR_URL_IMG
import com.iium.iium_medioz.util.`object`.Constant.SEND_CODE_TRUE
import com.iium.iium_medioz.util.`object`.Constant.SEND_ID
import com.iium.iium_medioz.util.`object`.Constant.SEND_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.SEND_NORMAL
import com.iium.iium_medioz.util.`object`.Constant.SEND_TEXTIMG
import com.iium.iium_medioz.util.`object`.Constant.SEND_TIME_STAMP
import com.iium.iium_medioz.util.`object`.Constant.SEND_TITLE
import com.iium.iium_medioz.util.`object`.Constant.SEND_VIDEO
import com.iium.iium_medioz.util.`object`.Constant.TAG
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import com.iium.iium_medioz.util.log.LLog
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class SendActivity : BaseActivity() {

    private lateinit var mBinding : ActivitySendBinding
    private lateinit var apiServices: APIService
    private var doubleBackToExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_send)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
        initView()
    }

    private fun initView() {
        val title = intent.getStringExtra(SEND_TITLE)
        val keyword = intent.getStringExtra(SEND_KEYWORD)
        val timestamp = intent.getStringExtra(SEND_TIME_STAMP)
        val textList = intent.getStringExtra(SEND_TEXTIMG)
        val normalList = intent.getStringExtra(SEND_NORMAL)
        val videoList = intent.getStringExtra(SEND_VIDEO)
        val id = intent.getStringExtra(SEND_ID)

        Log.d(TAG,"아이디 -> $id")
        Log.d(TAG,"타이틀 -> $title")
        Log.d(TAG,"키워드 -> $keyword")
        Log.d(TAG,"날짜 -> $timestamp")

        mBinding.tvMedicalDetailTitle.text = title.toString()
        mBinding.tvMedicalDetailData.text = timestamp.toString()
        mBinding.tvMyKeyword.text = keyword.toString()

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
                        else -> Log.d(LLog.TAG, "실패")
                    }
                }
                catch (e: Exception) {
                    LLog.d(e.toString())
                }
            }.start()
            initGone(str_idx)
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
                        else -> Log.d(LLog.TAG, "실패")
                    }
                }
                catch (e: Exception) {
                    LLog.d(e.toString())
                }
            }.start()
        }
    }

    private fun initGone(strIdx: String) {
        if(strIdx == "0") {
            if (mBinding.llFirst.visibility == View.GONE) {
                mBinding.llFirst.visibility = View.VISIBLE
            }
            mBinding.sbFirst.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                   mBinding.tvSkFirst.text = p1.toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            })
        }
        if(strIdx == "1") {
            if (mBinding.llSecond.visibility == View.GONE) {
                mBinding.llSecond.visibility = View.VISIBLE
            }
            mBinding.sbSecond.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    mBinding.tvSkSecond.text = p1.toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            })
        }
        if(strIdx == "2") {
            if (mBinding.llThird.visibility == View.GONE) {
                mBinding.llThird.visibility = View.VISIBLE
            }
            mBinding.sbThird.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    mBinding.tvSkThird.text = p1.toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            })
        }
        if(strIdx == "3") {
            if (mBinding.llFour.visibility == View.GONE) {
                mBinding.llFour.visibility = View.VISIBLE
            }
            mBinding.sbFour.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    mBinding.tvSkFour.text = p1.toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            })
        }
        if(strIdx == "4") {
            if (mBinding.llFive.visibility == View.GONE) {
                mBinding.llFive.visibility = View.VISIBLE
            }
            mBinding.sbFive.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                    mBinding.tvSkFive.text = p1.toString()
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }

            })
        }
    }

    //////////////////////텍스트 이미지 추출 API////////////////////////////
    private fun first(textFirst: String) {
        LLog.e("텍스트 첫번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(textFirst, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"텍스트 첫번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image1.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"텍스트 첫번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"텍스트 첫번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "텍스트 첫번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun second(second: String) {
        LLog.e("텍스트 두번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(second, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"텍스트 두번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image2.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"텍스트 두번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"텍스트 두번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "텍스트 두번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun third(third: String) {
        LLog.e("텍스트 세번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(third, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"텍스트 세번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image3.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"텍스트 세번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"텍스트 세번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "텍스트 세번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun four(four: String) {
        LLog.e("텍스트 네번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(four, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"텍스트 네번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image4.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"텍스트 네번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"텍스트 네번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "텍스트 네번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun five(five: String) {
        LLog.e("텍스트 다섯번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(five, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"텍스트 다섯번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.image5.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"텍스트 다섯번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"텍스트 다섯번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "텍스트 다섯번째 Fail -> ${t.localizedMessage}")
            }
        })
    }


    //////////////////////일반 이미지 추출 API////////////////////////////
    private fun normal_first(nm_first: String) {
        LLog.e("일반 첫번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_first, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"일반 첫번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage1.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"일반 첫번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"일반 첫번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "일반 첫번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun normal_second(nm_second: String) {
        LLog.e("일반 두번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_second, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"일반 두번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage2.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"일반 두번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"일반 두번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "일반 두번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun normal_third(nm_third: String) {
        LLog.e("일반 세번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_third, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"일반 세번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage3.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"일반 세번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"일반 세번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "일반 세번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun normal_four(nm_four: String) {
        LLog.e("일반 네번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_four, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"일반 네번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage4.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"일반 네번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"일반 네번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "일반 네번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun normal_five(nm_five: String) {
        LLog.e("일반 다섯번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_five, prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"일반 다섯번째 response SUCCESS -> $result")
                    Thread {
                        try {
                            val imgs = result.byteStream()
                            val bit = BitmapFactory.decodeStream(imgs)
                            val bitimage = Bitmap.createScaledBitmap(bit, 210,210,true)
                            mBinding.nImage5.setImageBitmap(bitimage)
                        }
                        catch (e: Exception) {
                            Log.d(LLog.TAG,"일반 다섯번째 이미지 다운 실패 -> $e")
                        }
                    }.start()
                }
                else {
                    Log.d(LLog.TAG,"일반 다섯번째  response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "일반 다섯번째 Fail -> ${t.localizedMessage}")

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
        window.statusBarColor = getColor(R.color.colorPrimary)
    }

    private fun sendAPI() {
        val requestHashMap : HashMap<String, RequestBody> = HashMap()

        val title = intent.getStringExtra(SEND_TITLE)
        val keyword = intent.getStringExtra(SEND_KEYWORD)
        val timestamp = intent.getStringExtra(SEND_TIME_STAMP)
        val textList = intent.getStringExtra(SEND_TEXTIMG)
        val normalList = intent.getStringExtra(SEND_NORMAL)
        val videoList = intent.getStringExtra(SEND_VIDEO)
        val id = intent.getStringExtra(SEND_ID)
        val send_sendcode = SEND_CODE_TRUE
        val send_default = DEFAULT_CODE_FALSE
        val send_sensitivity = "0"

        val send = DataSend(title, keyword, timestamp, send_sendcode, send_sensitivity,id,send_default)

        LLog.e("판매 데이터 API")
        val vercall: Call<SendModel> = apiServices.getChange(prefs.newaccesstoken,id,send)
        vercall.enqueue(object : Callback<SendModel> {
            override fun onResponse(call: Call<SendModel>, response: Response<SendModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"판매 데이터 response SUCCESS -> $result")
                    naverOCRAPI()
                    moveSaveSend()
                }
                else {
                    Log.d(LLog.TAG,"판매 데이터 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<SendModel>, t: Throwable) {
                Log.d(LLog.TAG, "판매 데이터 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun naverOCRAPI() {
        Thread {
            val apiURL = Constant.OCR_URL
            val secretKey = OCR_SECRET
            try {
                val url = URL(apiURL)
                val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                con.useCaches = false
                con.doInput = true
                con.doOutput = true
                con.requestMethod = "POST"
                con.setRequestProperty("Content-Type", "application/json; charset=utf-8")
                con.setRequestProperty("X-OCR-SECRET", secretKey)
                val json = JSONObject()
                json.put("version", "V2")
                json.put("requestId", UUID.randomUUID().toString())
                json.put("timestamp", System.currentTimeMillis())
                json.put("lang","ko")
                val image = JSONObject()
                image.put("format", "jpg")
                image.put(
                    "url", OCR_URL_IMG
                )
                image.put("name", "demo")

                val images = JSONArray()
                images.put(image)
                json.put("images", images)

                val postParams: String = json.toString()
                val wr = DataOutputStream(con.outputStream)
                wr.writeBytes(postParams)
                wr.flush()
                wr.close()
                val responseCode: Int = con.responseCode
                val br: BufferedReader = if (responseCode == 200) {
                    BufferedReader(InputStreamReader(con.inputStream))
                } else {
                    BufferedReader(InputStreamReader(con.errorStream))
                }
                var inputLine: String?
                val response = StringBuffer()
                while (br.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                br.close()
                println(response)
                Log.d(TAG,"네이버 테스트 OCR -> $response")
            } catch (e: java.lang.Exception) {
                println(e)
            }
        }.start()
    }

    fun onBackPressed(v: View?) {
        moveDetail()
    }

    fun onSendClick(v: View?) {
        naverOCRAPI()
//        sendAPI()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (doubleBackToExit) {
            finishAffinity()
        } else {
            doubleBackToExit = true
            runDelayed(1500L) {
                doubleBackToExit = false
            }
        }
    }

    private fun runDelayed(millis: Long, function: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed(function, millis)
    }
}