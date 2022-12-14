package com.iium.iium_medioz.viewmodel.main.bottom

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.datasource.GetDataDeleteDataSource
import com.iium.iium_medioz.model.datasource.GetImgDataSource
import com.iium.iium_medioz.util.base.SingleLiveEvent
import com.iium.iium_medioz.util.common.Event
import com.iium.iium_medioz.util.log.LLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getImgDataSource: GetImgDataSource,
    private val getDataDeleteDataSource: GetDataDeleteDataSource,
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

    val count = MutableLiveData<String>("0")
    val normalCount = MutableLiveData<String>("0")
    val vedioCount = MutableLiveData<String>("0")
    val medicalDetailTitle = MutableLiveData<String>("0")
    val medicalDetailData = MutableLiveData<String>("0")
    val myKeyword = MutableLiveData<String>("0")

    var id: String = ""

    private val _viewEvent = MutableLiveData<Event<Any>>()
    val viewEvent: LiveData<Event<Any>>
        get() = _viewEvent

    fun viewEvent(content: Any) {
        _viewEvent.postValue(Event(content))
    }

    //////////////////////????????? ????????? ?????? API////////////////////////////
    private fun getTextImg(str_idx: Int, text: String, token: String) {
        LLog.e("????????? ${str_idx + 1}?????? ????????? API")
        viewModelScope.launch {
            when (val result = getImgDataSource.invoke(text, token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "????????? ${str_idx + 1}?????? response SUCCESS -> ${result.data}")
                    val imgs = result.data?.byteStream()
                    val bit = BitmapFactory.decodeStream(imgs)
                    val bitimage = Bitmap.createScaledBitmap(bit, 210, 210, true)
                    when (str_idx) {
                        0 -> image1.postValue(bitimage)
                        1 -> image2.postValue(bitimage)
                        2 -> image3.postValue(bitimage)
                        3 -> image4.postValue(bitimage)
                        4 -> image5.postValue(bitimage)
                        else -> Log.d(LLog.TAG, "??????")
                    }
                }
                is Result.Error -> {
                    mutableErrorMessage.postValue("${result.code} ${result.message}")
                    Log.d(LLog.TAG, "????????? ${str_idx + 1}?????? response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    mutableErrorMessage.postValue(result.e.localizedMessage)
                    Log.d(LLog.TAG, "????????? ${str_idx + 1}?????? Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    //////////////////////?????? ????????? ?????? API////////////////////////////
    private fun getNormalImg(str_idx: Int, text: String, token: String) {
        LLog.e("?????? ${str_idx + 1}?????? ????????? API")
        viewModelScope.launch {
            when (val result = getImgDataSource.invoke(text, token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "?????? ${str_idx + 1}?????? response SUCCESS -> ${result.data}")
                    val imgs = result.data?.byteStream()
                    val bit = BitmapFactory.decodeStream(imgs)
                    val bitimage = Bitmap.createScaledBitmap(bit, 210, 210, true)
                    when (str_idx) {
                        0 -> nImage1.postValue(bitimage)
                        1 -> nImage2.postValue(bitimage)
                        2 -> nImage3.postValue(bitimage)
                        3 -> nImage4.postValue(bitimage)
                        4 -> nImage5.postValue(bitimage)
                        else -> Log.d(LLog.TAG, "??????")
                    }
                }
                is Result.Error -> {
                    mutableErrorMessage.postValue("${result.code} ${result.message}")
                    Log.d(LLog.TAG, "?????? ${str_idx + 1}?????? response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    mutableErrorMessage.postValue(result.e.localizedMessage)
                    Log.d(LLog.TAG, "?????? ${str_idx + 1}?????? Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    //////////////////////?????? ?????? API////////////////////////////
    private fun getVideo(str_idx: Int, text: String, token: String) {
        LLog.e("????????? ??????_${str_idx + 1}?????? API")
        viewModelScope.launch {
            when (val result = getImgDataSource.invoke(text, token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "????????? ??????_${str_idx + 1}?????? response SUCCESS -> ${result.data}")
                }
                is Result.Error -> {
                    mutableErrorMessage.postValue("${result.code} ${result.message}")
                    Log.d(LLog.TAG, "????????? ??????_${str_idx + 1}?????? response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    mutableErrorMessage.postValue(result.e.localizedMessage)
                    Log.d(LLog.TAG, "????????? ??????_${str_idx + 1}?????? Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    fun setViewData(
        title: String?,
        keyword: String?,
        timestamp: String?,
        textList: String?,
        normalList: String?,
        videoList: String?
    ) {
        Log.d(LLog.TAG, "????????? ????????? : ${textList.toString()}")
        Log.d(LLog.TAG, "?????? ????????? : ${normalList.toString()}")
        Log.d(LLog.TAG, "????????? ????????? : ${videoList.toString()}")

        val img = textList?.substring(2) ?: ""
        val imgtest = img.substring(0, img.length - 2)
        val tnla = imgtest.split(",")

        for (i in tnla.indices step (1)) {
            getTextImg(i, tnla[i].trim(), newToken)
        }
        count.postValue(tnla.count().toString())

        val normal = normalList?.substring(2) ?: ""
        val normaltest = normal.substring(0, normal.length - 2)
        val normalstart = normaltest.split(",")

        for (i in normalstart.indices step (1)) {
            getNormalImg(i, normalstart[i].trim(), newToken)
        }
        normalCount.postValue(normalstart.count().toString())

        val video = videoList?.substring(2) ?: ""
        val videotest = video.substring(0, video.length - 2)
        val videostart = videotest.split(",")

        for (i in videostart.indices step (1)) {
            getVideo(i, videostart[i].trim(), newToken)
        }
        vedioCount.postValue(videostart.count().toString())

        medicalDetailTitle.value = title.toString()
        medicalDetailData.value = timestamp.toString()
        myKeyword.value = keyword.toString()
    }

    fun initDelete(id: String = "") {
        LLog.e("????????? ?????? API")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getDataDeleteDataSource.invoke(id, newToken)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "????????? ?????? response SUCCESS -> ${result.data}")
                }
                is Result.Error -> {
                    Log.d(LLog.TAG, "????????? ??????  response ERROR -> $id")
                    mutableErrorMessage.postValue("????????? ????????? ?????????????????????.")
                    viewEvent(DataModifyViewModel.NAVIGATE_MAIN_ACTIVITY)
                }
                is Result.Exception -> {

                    Log.d(LLog.TAG, "????????? ?????? Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    companion object {
        const val NAVIGATE_MAIN_ACTIVITY = 22212
        const val SHOW_ERROR_DIALOG = 55555
    }
}