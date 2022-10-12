package com.iium.iium_medioz.viewmodel.main.bottom.data.send

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.model.OCRModel
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.datasource.GetImgDataSource
import com.iium.iium_medioz.model.datasource.PostDataSendDataSource
import com.iium.iium_medioz.model.datasource.PostOCRDataSource
import com.iium.iium_medioz.model.send.DataSend
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.base.SingleLiveEvent
import com.iium.iium_medioz.util.common.Event
import com.iium.iium_medioz.util.log.LLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendViewModel(
    private val getImgDataSource: GetImgDataSource,
    private val postDataSendDataSource: PostDataSendDataSource,
    private val postOCRDataSource: PostOCRDataSource,
    private val newToken: String
) : ViewModel() {

    lateinit var title: String
    lateinit var keyword: String
    lateinit var timestamp: String
    lateinit var textList: String
    lateinit var normalList: String
    lateinit var videoList: String
    lateinit var id: String
    lateinit var count: String
    lateinit var normalCount: String
    lateinit var videoCount: String

    val dataFirst = MutableLiveData<String>("")
    val dataSecond = MutableLiveData<String>("")
    val dataThird = MutableLiveData<String>("")
    val dataFour = MutableLiveData<String>("")
    val dataFive = MutableLiveData<String>("")

    val image1 = MutableLiveData<Bitmap>()
    val image2 = MutableLiveData<Bitmap>()
    val image3 = MutableLiveData<Bitmap>()
    val image4 = MutableLiveData<Bitmap>()
    val image5 = MutableLiveData<Bitmap>()

    val nImage1 = MutableLiveData<Bitmap>()
    val nImage2 = MutableLiveData<Bitmap>()
    val nImage3 = MutableLiveData<Bitmap>()
    val nImage4 = MutableLiveData<Bitmap>()
    val nImage5 = MutableLiveData<Bitmap>()

    val pvFirst = MutableLiveData<Bitmap>()
    val pvSecond = MutableLiveData<Bitmap>()
    val pvThird = MutableLiveData<Bitmap>()

    val mutableErrorMessage = SingleLiveEvent<String>()

    private val _viewEvent = MutableLiveData<Event<Any>>()
    val viewEvent: LiveData<Event<Any>>
        get() = _viewEvent

    fun viewEvent(content: Any) {
        _viewEvent.postValue(Event(content))
    }

    fun sendAPI() {
        val send_sendcode = Constant.SEND_CODE_TRUE
        val send_default = Constant.DEFAULT_CODE_FALSE

        val send = DataSend(
            title,
            keyword,
            textList,
            normalList,
            videoList,
            timestamp,
            send_default,
            dataFirst.value ?: "",
            dataSecond.value ?: "",
            dataThird.value ?: "",
            dataFour.value ?: "",
            dataFive.value ?: "",
            send_sendcode,
            id
        )

        LLog.e("판매 데이터 API")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = postDataSendDataSource.invoke(newToken, send)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "판매 데이터 response SUCCESS -> ${result.data}")
//                    naverOCRAPI()
                    viewEvent(NAVIGATE_SAVE_SEND_ACTIVITY)
                }
                is Result.Error -> {
                    mutableErrorMessage.postValue("${result.code} ${result.message}")
                    Log.d(LLog.TAG, "판매 데이터 response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    mutableErrorMessage.postValue(result.e.localizedMessage)
                    Log.d(LLog.TAG, "판매 데이터 Fail ->${result.e.localizedMessage}")
                }
            }
        }
    }

    //////////////////////텍스트 이미지 추출 API////////////////////////////
    private fun getTextImg(str_idx: Int, text: String, token: String) {
        LLog.e("텍스트 ${str_idx + 1}번째 이미지 API")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getImgDataSource.invoke(text, token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "텍스트 ${str_idx + 1}번째 response SUCCESS -> ${result.data}")
                    val imgs = result.data.byteStream()
                    val bit = BitmapFactory.decodeStream(imgs)
                    val bitimage = Bitmap.createScaledBitmap(bit, 210, 210, true)
                    when (str_idx) {
                        0 -> image1.postValue(bitimage)
                        1 -> image2.postValue(bitimage)
                        2 -> image3.postValue(bitimage)
                        3 -> image4.postValue(bitimage)
                        4 -> image5.postValue(bitimage)
                        else -> Log.d(LLog.TAG, "실패")
                    }
                }
                is Result.Error -> {
                    mutableErrorMessage.postValue("${result.code} ${result.message}")
                    Log.d(LLog.TAG, "텍스트 ${str_idx + 1}번째 response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    mutableErrorMessage.postValue(result.e.localizedMessage)
                    Log.d(LLog.TAG, "텍스트 ${str_idx + 1}번째 Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    //////////////////////일반 이미지 추출 API////////////////////////////
    private fun getNormalImg(str_idx: Int, text: String, token: String) {
        LLog.e("일반 ${str_idx + 1}번째 이미지 API")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getImgDataSource.invoke(text, token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "일반 ${str_idx + 1}번째 response SUCCESS -> ${result.data}")
                    val imgs = result.data.byteStream()
                    val bit = BitmapFactory.decodeStream(imgs)
                    val bitimage = Bitmap.createScaledBitmap(bit, 210, 210, true)
                    when (str_idx) {
                        0 -> nImage1.postValue(bitimage)
                        1 -> nImage2.postValue(bitimage)
                        2 -> nImage3.postValue(bitimage)
                        3 -> nImage4.postValue(bitimage)
                        4 -> nImage5.postValue(bitimage)
                        else -> Log.d(LLog.TAG, "실패")
                    }
                }
                is Result.Error -> {
                    mutableErrorMessage.postValue("${result.code} ${result.message}")
                    Log.d(LLog.TAG, "일반 ${str_idx + 1}번째 response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    mutableErrorMessage.postValue(result.e.localizedMessage)
                    Log.d(LLog.TAG, "일반 ${str_idx + 1}번째 Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    //////////////////////영상 추출 API////////////////////////////
    private fun getVideo(str_idx: Int, text: String, token: String) {
        LLog.e("비디오 추출_${str_idx + 1}번째 API")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getImgDataSource.invoke(text, token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "비디오 추출_${str_idx + 1}번째 response SUCCESS -> ${result.data}")
                }
                is Result.Error -> {
                    mutableErrorMessage.postValue("${result.code} ${result.message}")
                    Log.d(LLog.TAG, "비디오 추출_${str_idx + 1}번째 response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    mutableErrorMessage.postValue(result.e.localizedMessage)
                    Log.d(LLog.TAG, "비디오 추출_${str_idx + 1}번째 Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    private fun OCRAPI(response: StringBuffer) {
        LLog.e("네이버 OCR API")
        val ocrModel = OCRModel(response)
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = postOCRDataSource.invoke(newToken, ocrModel)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "네이버 OCR response SUCCESS ->  ${result.data}")
                    //                    moveSaveSend()
                    viewEvent(NAVIGATE_MAKE_PDF_ACTIVITY)
                }
                is Result.Error -> {
                    mutableErrorMessage.postValue("${result.code} ${result.message}")
                    Log.d(LLog.TAG, "네이버 OCR response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    mutableErrorMessage.postValue(result.e.localizedMessage)
                    Log.d(LLog.TAG, "네이버 OCR Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    fun setViewData(
        inputTitle: String?,
        inputKeyword: String?,
        inputTimestamp: String?,
        inputTextList: String?,
        inputNormalList: String?,
        inputVideoList: String?,
        inputId: String?
    ) {
        Log.d(LLog.TAG, "텍스트 이미지 : ${inputTextList.toString()}")
        Log.d(LLog.TAG, "일반 이미지 : ${inputNormalList.toString()}")
        Log.d(LLog.TAG, "비디오 이미지 : ${inputVideoList.toString()}")

        val img = inputTextList?.substring(2)
        val imgtest = img?.substring(0, img.length - 2)
        val tnla = imgtest?.split(",")
        if (inputId != null) {
            this.id = inputId
        }
        for (i in 0 until tnla?.size!! step (1)) {
            getTextImg(i, tnla[i].trim(), newToken)
        }
        count = tnla.count().toString()

        val normal = inputNormalList?.substring(2)
        val normaltest = normal?.substring(0, normal.length - 2)
        val normalstart = normaltest?.split(",")

        for (i in 0 until normalstart?.size!! step (1)) {
            getNormalImg(i, tnla[i].trim(), newToken)
        }
        normalCount = normalstart.count().toString()

        val video = inputVideoList?.substring(2)
        val videotest = video?.substring(0, video.length - 2)
        val videostart = videotest?.split(",")

        for (i in 0 until videostart?.size!! step (1)) {
            getVideo(i, videostart[i].trim(), newToken)
        }
        videoCount = videostart.count().toString()

        title = inputTitle ?: ""
        keyword = inputKeyword ?: ""
        timestamp = inputTimestamp ?: ""
        textList = inputTextList ?: ""
        normalList = inputNormalList ?: ""
        videoList = inputVideoList ?: ""
    }


    companion object {
        const val NAVIGATE_SAVE_SEND_ACTIVITY =
            33333 // 얘네도 base객체로 넘겨서 처리하는건 어떨지?, intent넘기는것도 더 편한방법..
        const val NAVIGATE_MAKE_PDF_ACTIVITY = 44444
    }

}