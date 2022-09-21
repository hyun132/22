package com.iium.iium_medioz.view.main.bottom.data

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.iium.iium_medioz.R
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.ApiUtils
import com.iium.iium_medioz.databinding.ActivityDataModifyBinding
import com.iium.iium_medioz.databinding.ActivitySaveBinding
import com.iium.iium_medioz.model.upload.DeleteModel
import com.iium.iium_medioz.model.upload.ModifyList
import com.iium.iium_medioz.model.upload.ModifyModel
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_ID
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_KEYWORD
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_NORMAL
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_TEXTIMG
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_TITLE
import com.iium.iium_medioz.util.`object`.Constant.DATA_MODIFY_VIDEO
import com.iium.iium_medioz.util.base.BaseActivity
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.util.log.LLog
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataModifyActivity : BaseActivity() {

    private lateinit var mBinding : ActivityDataModifyBinding
    private lateinit var apiServices: APIService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_data_modify)
        mBinding.activity = this
        apiServices = ApiUtils.apiService
        mBinding.lifecycleOwner = this
        inStatusBar()
        runOnUiThread {
            initView()
        }
    }

    private fun inStatusBar() {
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
        window.statusBarColor = getColor(R.color.colorPrimary)
    }

    private fun initView() {
        val title = intent?.getStringExtra(DATA_MODIFY_TITLE)
        val keyword = intent?.getStringExtra(DATA_MODIFY_KEYWORD)
        val textList = intent?.getStringExtra(DATA_MODIFY_TEXTIMG)
        val normalList = intent?.getStringExtra(DATA_MODIFY_NORMAL)
        val videoList = intent?.getStringExtra(DATA_MODIFY_VIDEO)

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
                    mBinding.tvTextCount.text = tnla.count().toString()
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
                        else -> Log.d(LLog.TAG, "실패")
                    }
                    mBinding.tvNormalCount.text = normalstart.count().toString()
                }
                catch (e: Exception) {
                    LLog.d(e.toString())
                }
            }.start()
        }

        val video =  videoList?.substring(2)
        val videotest = video?.substring(0, video.length - 2)
        val videostart = videotest?.split(",")

        for(i in 0 until videostart?.size!! step(1)) {
            val str_idx = i.toString()
            when (str_idx) {
                "0" -> video_first(videostart[i].trim())
                "1" -> video_second(videostart[i].trim())
                "2" -> video_third(videostart[i].trim())
                else -> Log.d(LLog.TAG,"실패")
            }

            if (videostart.count() in 0..3) {
                if( mBinding.pvFirst.visibility == View.GONE) {
                    mBinding.pvFirst.visibility = View.VISIBLE
                } else if(mBinding.pvSecond.visibility == View.GONE) {
                    mBinding.pvSecond.visibility = View.VISIBLE
                } else if(mBinding.pvThird.visibility == View.GONE) {
                    mBinding.pvThird.visibility = View.VISIBLE
                }
            }
            mBinding.tvVideoCount.text = videostart.count().toString()

        }

        mBinding.etNickname.text = Editable.Factory.getInstance().newEditable(title.toString())
        mBinding.etKeyword.text = Editable.Factory.getInstance().newEditable(keyword.toString())

    }

    //////////////////////텍스트 이미지 추출 API////////////////////////////
    private fun first(textFirst: String) {
        LLog.e("텍스트 첫번째 이미지 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(textFirst, MyApplication.prefs.newaccesstoken)
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
        val vercall: Call<ResponseBody> = apiServices.getImg(second, MyApplication.prefs.newaccesstoken)
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
        val vercall: Call<ResponseBody> = apiServices.getImg(third, MyApplication.prefs.newaccesstoken)
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
        val vercall: Call<ResponseBody> = apiServices.getImg(four, MyApplication.prefs.newaccesstoken)
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
        val vercall: Call<ResponseBody> = apiServices.getImg(five, MyApplication.prefs.newaccesstoken)
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
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_first, MyApplication.prefs.newaccesstoken)
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
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_second, MyApplication.prefs.newaccesstoken)
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
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_third, MyApplication.prefs.newaccesstoken)
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
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_four, MyApplication.prefs.newaccesstoken)
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
        val vercall: Call<ResponseBody> = apiServices.getImg(nm_five, MyApplication.prefs.newaccesstoken)
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
        LLog.e("비디오 추출_첫번째 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(s, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"비디오 추출_첫번째 response SUCCESS -> $result")

                }
                else {
                    Log.d(LLog.TAG,"비디오 추출_첫번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "비디오 추출_첫번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun video_second(s: String) {
        LLog.e("비디오 추출_두번째 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(s, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"비디오 추출_두번째 response SUCCESS -> $result")
                }
                else {
                    Log.d(LLog.TAG,"비디오 추출_두번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "비디오 추출_두번째 Fail -> ${t.localizedMessage}")
            }
        })
    }

    private fun video_third(s: String) {
        LLog.e("비디오 추출_세번째 API")
        val vercall: Call<ResponseBody> = apiServices.getImg(s, MyApplication.prefs.newaccesstoken)
        vercall.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"비디오 추출_세번째 response SUCCESS -> $result")
                }
                else {
                    Log.d(LLog.TAG,"비디오 추출_세번째 response ERROR -> $result")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(LLog.TAG, "비디오 추출_세번째 Fail -> ${t.localizedMessage}")
            }
        })
    }


    fun onPutClick(v: View) {
        val dlg: AlertDialog.Builder = AlertDialog.Builder(this,  android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
        dlg.setTitle("데이터 수정") //제목
        dlg.setMessage("데이터를 수정 하시겠습니까?") // 메시지
        dlg.setPositiveButton("확인") { dialog, which ->
            initModify()
            dialog.dismiss()
        }
        dlg.setNegativeButton("취소") { dialog, which ->
            dialog.dismiss()
        }
        dlg.show()
    }

    private fun initModify() {
        val id = intent.getStringExtra(DATA_MODIFY_ID)
        val title = mBinding.etNickname.text.toString()
        val keyword = mBinding.etKeyword.text.toString()
        val data = ModifyList(title, keyword)
        LLog.e("데이터 수정 API")
        val vercall: Call<ModifyModel> = apiServices.putData(MyApplication.prefs.newaccesstoken,id,data)
        vercall.enqueue(object : Callback<ModifyModel> {
            override fun onResponse(call: Call<ModifyModel>, response: Response<ModifyModel>) {
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    Log.d(LLog.TAG,"데이터 수정 response SUCCESS -> $result")
                }
                else {
                    Log.d(LLog.TAG,"데이터 수정  response ERROR -> $id")
                    ErrorDialog()
                }
            }
            override fun onFailure(call: Call<ModifyModel>, t: Throwable) {
                Log.d(LLog.TAG, "데이터 수정 Fail -> ${t.localizedMessage}")
                moveMain()
            }
        })
    }
}