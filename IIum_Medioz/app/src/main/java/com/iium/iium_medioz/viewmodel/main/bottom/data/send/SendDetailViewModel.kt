package com.iium.iium_medioz.viewmodel.main.bottom.data.send

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.datasource.GetImgDataSource
import com.iium.iium_medioz.model.datasource.GetSendDeleteDataSource
import com.iium.iium_medioz.util.base.SingleLiveEvent
import com.iium.iium_medioz.util.common.Event
import com.iium.iium_medioz.util.log.LLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SendDetailViewModel(
    private val getImgDataSource: GetImgDataSource,
    private val getSendDeleteDataSource: GetSendDeleteDataSource,
    private val newToken: String
) : ViewModel() {
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

//    val pvFirst = MutableLiveData<Bitmap>()
//    val pvSecond = MutableLiveData<Bitmap>()
//    val pvThird = MutableLiveData<Bitmap>()

    val mutableErrorMessage = SingleLiveEvent<String>()

//    val count = MutableLiveData<String>("0")
//    val normalCount = MutableLiveData<String>("0")
//    val vedioCount = MutableLiveData<String>("0")

    val medicalDetailTitle = MutableLiveData<String>("0")
    val myKeyword = MutableLiveData<String>("0")
    val timestamp = MutableLiveData<String>("0")

    var id: String = ""

    private val _viewEvent = MutableLiveData<Event<Any>>()
    val viewEvent: LiveData<Event<Any>>
        get() = _viewEvent

    private fun viewEvent(content: Any) {
        _viewEvent.postValue(Event(content))
    }

    //////////////////////텍스트 이미지 추출 API////////////////////////////
    private fun getTextImg(str_idx: Int, text: String, token: String) {
        LLog.e("텍스트 ${str_idx + 1}번째 이미지 API")
        viewModelScope.launch {
            when (val result = getImgDataSource.invoke(text, token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "텍스트 ${str_idx + 1}번째 response SUCCESS -> ${result.data}")
                    val imgs = result.data?.byteStream()
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
                    Log.d(LLog.TAG, "텍스트 ${str_idx + 1}번째 response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "텍스트 ${str_idx + 1}번째 Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    //////////////////////일반 이미지 추출 API////////////////////////////
    private fun getNormalImg(str_idx: Int, text: String, token: String) {
        LLog.e("일반 ${str_idx + 1}번째 이미지 API")
        viewModelScope.launch {
            when (val result = getImgDataSource.invoke(text, token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "일반 ${str_idx + 1}번째 response SUCCESS -> ${result.data}")
                    val imgs = result.data?.byteStream()
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
                    Log.d(LLog.TAG, "일반 ${str_idx + 1}번째 response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
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
                    Log.d(LLog.TAG, "비디오 추출_${str_idx + 1}번째 response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "비디오 추출_${str_idx + 1}번째 Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    fun setViewData(
        inputTitle: String?,
        inputKeyword: String?,
        inputTimestamp: String?,
        textList: String?,
        normalList: String?,
        videoList: String?,
        inputId: String?
    ) {
        Log.d(LLog.TAG, "텍스트 이미지 : ${textList.toString()}")
        Log.d(LLog.TAG, "일반 이미지 : ${normalList.toString()}")
        Log.d(LLog.TAG, "비디오 이미지 : ${videoList.toString()}")
        medicalDetailTitle.value = inputTitle ?: ""
        timestamp.value = inputTimestamp ?: ""
        myKeyword.value = inputKeyword ?: ""
        id=inputId?:""

        val img = textList?.substring(2)
        val imgtest = img?.substring(0, img.length - 2)
        val tnla = imgtest?.split(",")

        if (tnla != null) {
            for (i in tnla.indices step (1)) {
                getTextImg(i, tnla[i].trim(), newToken)
            }
        }
//        if (tnla != null) {
//            viewModel.count.postValue(tnla.count().toString())
//        }

        val normal = normalList?.substring(2)
        val normaltest = normal?.substring(0, normal.length - 2)
        val normalstart = normaltest?.split(",")

        if (normalstart != null) {
            for (i in normalstart.indices step (1)) {
                tnla?.get(i)?.let { getNormalImg(i, it.trim(), newToken) }
            }
        }
//        if (normalstart != null) {
//            viewModel.normalCount.postValue(normalstart.count().toString())
//        }

//        val video = videoList?.substring(2)
//        val videotest = video?.substring(0, video.length - 2)
//        val videostart = videotest?.split(",")
//
//        for (i in 0 until videostart?.size!! step (1)) {
//            getVideo(i,videostart[i].trim(),newToken)
//        }
//        vedioCount.postValue(videostart.count().toString())
    }

    fun initDelete() {
        LLog.e("데이터 삭제 API")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getSendDeleteDataSource.invoke(newToken,id)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "판매 데이터 해제 response SUCCESS -> ${result.data}")
                    mutableErrorMessage.postValue("판매 해제가 완료되었습니다.")
                    viewEvent(NAVIGATE_MAIN_ACTIVITY)
                }
                is Result.Error -> {
                    Log.d(LLog.TAG, "판매 데이터 해제 response ERROR -> ${result.message} ${result.code}")
                    viewEvent(SHOW_ERROR_DIALOG)
                }
                is Result.Exception -> {
                    viewEvent(SHOW_ERROR_DIALOG)
                    Log.d(LLog.TAG, "판매 데이터 해제 Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    companion object {
        const val NAVIGATE_MAIN_ACTIVITY = 33333
        const val SHOW_ERROR_DIALOG = 55555
    }
}