package com.iium.iium_medioz.view.main.bottom.data.send

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivitySendBinding
import com.iium.iium_medioz.model.OCRModel
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
import com.iium.iium_medioz.view.main.MainActivity
import com.iium.iium_medioz.viewmodel.main.bottom.data.send.SendViewModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.w3c.dom.Text
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
    private val viewModel : SendViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_send)
        apiServices = ApiUtils.apiService
        mBinding.apply {
            activity = this@SendActivity
            lifecycleOwner = this@SendActivity
            viewModel = this@SendActivity.viewModel
        }
        inStatusBar()
        runOnUiThread {
            initView()
        }
        viewModel.viewEvent.observe(this){
            it.getContentIfNotHandled()?.let { event ->
                when(event){
                    SendViewModel.NAVIGATE_SAVE_SEND_ACTIVITY -> moveSaveSend()
                    SendViewModel.NAVIGATE_MAKE_PDF_ACTIVITY -> movePDF()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
    }

    private fun initView() {
        val title = intent.getStringExtra(SEND_TITLE)
        val keyword = intent.getStringExtra(SEND_KEYWORD)
        val timestamp = intent.getStringExtra(SEND_TIME_STAMP)
        val textList = intent.getStringExtra(SEND_TEXTIMG)
        val normalList = intent.getStringExtra(SEND_NORMAL)
        val videoList = intent.getStringExtra(SEND_VIDEO)
        val id = intent.getStringExtra(SEND_ID)

        viewModel.setViewData(title,keyword,timestamp,textList,normalList,videoList,id)
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.colorPrimary)
    }
//
//    private fun naverOCRAPI() {
//        Thread {
//            val apiURL = Constant.OCR_URL
//            val secretKey = OCR_SECRET
//            try {
//                val url = URL(apiURL)
//                val con: HttpURLConnection = url.openConnection() as HttpURLConnection
//                con.useCaches = false
//                con.doInput = true
//                con.doOutput = true
//                con.requestMethod = "POST"
//                con.setRequestProperty("Content-Type", "application/json; charset=utf-8")
//                con.setRequestProperty("X-OCR-SECRET", secretKey)
//                val json = JSONObject()
//                json.put("version", "V2")
//                json.put("requestId", UUID.randomUUID().toString())
//                json.put("timestamp", System.currentTimeMillis())
//                json.put("lang","ko")
//                val image = JSONObject()
//                image.put("format", "jpg")
//                image.put(
//                    "url", OCR_URL_IMG
//                )
//                image.put("name", "demo")
//
//                val images = JSONArray()
//                images.put(image)
//                json.put("images", images)
//
//                val postParams: String = json.toString()
//                val wr = DataOutputStream(con.outputStream)
//                wr.writeBytes(postParams)
//                wr.flush()
//                wr.close()
//                val responseCode: Int = con.responseCode
//                val br: BufferedReader = if (responseCode == 200) {
//                    BufferedReader(InputStreamReader(con.inputStream))
//                } else {
//                    BufferedReader(InputStreamReader(con.errorStream))
//                }
//                var inputLine: String?
//                val response = StringBuffer()
//                while (br.readLine().also { inputLine = it } != null) {
//                    response.append(inputLine)
//                }
//                Thread {
//                    OCRAPI(response)
//                }.start()
//                br.close()
//            } catch (e: java.lang.Exception) {
//                println(e)
//            }
//        }.start()
//    }
//
//    private fun OCRAPI(response: StringBuffer) {
//        LLog.e("네이버 OCR API")
//
//        val ocrModel = OCRModel(response)
//        val vercall: Call<OCRModel> = apiServices.postOCR(prefs.newaccesstoken,ocrModel)
//        vercall.enqueue(object : Callback<OCRModel> {
//            override fun onResponse(call: Call<OCRModel>, response: Response<OCRModel>) {
//                val result = response.body()
//                if (response.isSuccessful && result != null) {
//                    Log.d(LLog.TAG,"네이버 OCR response SUCCESS -> $result")
////                    moveSaveSend()
//                    movePDF()
//                }
//                else {
//                    Log.d(LLog.TAG,"네이버 OCR response ERROR -> $result")
//                }
//            }
//            override fun onFailure(call: Call<OCRModel>, t: Throwable) {
//                Log.d(LLog.TAG, "네이버 OCR Fail -> ${t.localizedMessage}")
//            }
//        })
//    }


    fun onBackPressed(v: View?) {
        moveMain()
    }

    fun onSendClick(v: View?) {
//        naverOCRAPI()
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle("판매 등록") //제목
        dlg.setMessage("판매등록을 하시겠습니까?") // 메시지
        dlg.setPositiveButton("확인") { dialog, which ->
            viewModel.sendAPI()
            dialog.dismiss()
        }
        dlg.setNegativeButton("취소") { dialog, which ->
            dialog.dismiss()
        }
        dlg.show()
    }

//    private fun runDelayed(millis: Long, function: () -> Unit) {
//        Handler(Looper.getMainLooper()).postDelayed(function, millis)
//    }

    override fun onDestroy() {
        super.onDestroy()
        finishAffinity()
    }
}

