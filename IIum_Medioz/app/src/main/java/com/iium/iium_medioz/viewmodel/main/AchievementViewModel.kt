package com.iium.iium_medioz.viewmodel.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.R
import com.iium.iium_medioz.model.datasource.*
import com.iium.iium_medioz.util.base.SingleLiveEvent
import com.iium.iium_medioz.util.log.LLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AchievementViewModel(
    private val remoteUserDataSource: GetUserDataUseCase,
    private val getMedicalDataSource: GetMedicalDataSource,
    private val getFeelDataSource: GetFeelDataUseCase,
    private val getSendDataSource: GetSendDataUseCase,
    private val getDocumentDataSource: GetDocumentDataUseCase,
    private val getProfileImageDataSource: GetProfileImageDataSource,
    private val token: String,
    private val newToken: String,
) : ViewModel() {

    val mutableErrorMessage = SingleLiveEvent<String>()
    private val _nickName = MutableLiveData<String>()
    val nickname: LiveData<String>
        get() = _nickName
    val isLoading = MutableLiveData<Boolean>(false)
    val achivImgSrc = MutableLiveData<Int>()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        mutableErrorMessage.value = "Exception handled: ${throwable.localizedMessage}"
    }

    //이건 그냥 통째로 view에 넘겨서 bindingadapter로 처리하는 방법 ?
    val ach1 = MutableLiveData<Int>()
    val ach2 = MutableLiveData<Int>()
    val ach3 = MutableLiveData<Int>()
    val ach4 = MutableLiveData<Int>()
    val ach5 = MutableLiveData<Int>()
    val ach6 = MutableLiveData<Int>()
    val ach7 = MutableLiveData<Int>()
    val achProfileImg = MutableLiveData<Bitmap>()

    init {
        initUser()
        initData()
        initcalendar()
        initSendData()
        initDocument()
    }

    private fun initData() {
        viewModelScope.launch(exceptionHandler) {
            isLoading.value = true
            when (val result = getMedicalDataSource.invoke(token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "나의 의료데이터 조회 SUCCESS -> $result ")
                    val calendarSize = result.data?.datalist?.size ?: 0
                    when {
                        (calendarSize in 1..1000) -> ach1.postValue(R.drawable.ach_1)
                        (calendarSize in 10..1000) -> ach5.postValue(R.drawable.ach_5)
                        (calendarSize in 20..1000) -> ach6.postValue(R.drawable.ach_6)
                        (calendarSize in 30..1000) -> ach7.postValue(R.drawable.ach_7)
                        else -> {
                            null
                        }
                    }
                }
                is Result.Error -> {
                    mutableErrorMessage.value = "${result.code} ${result.message}"
                    Log.d(LLog.TAG, "나의 의료데이터 조회ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "나의 의료데이터 조회 ERROR -> ${result.e.message}")
                }
            }
        }
    }

    private fun initUser() {
        LLog.e("회원 정보 API")
        viewModelScope.launch(exceptionHandler) {
            isLoading.value = true
            when (val result = remoteUserDataSource.invoke(token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "GetUser API SUCCESS -> ${result.data}")
                    _nickName.postValue(result.data?.user?.name ?: "홍길동")
                    result.data?.user?.imgName?.let { img -> profileImg(img, newToken) }
                }
                is Result.Error -> {
                    Log.d(LLog.TAG, "GetUser API ERROR -> ${result.message}")
                    otherAPI()
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "GetUser ERROR -> ${result.e.message}")
                }
            }
        }
    }

    private fun otherAPI() {
        LLog.e("회원 정보_두번째 API")
        viewModelScope.launch(exceptionHandler) {
            isLoading.value = true
            when (val result = remoteUserDataSource.invoke(newToken)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "GetUser Second API SUCCESS -> ${result.data}")
                    _nickName.postValue(result.data?.user?.name ?: "홍길동")
                    result.data?.user?.imgName?.let { img -> profileImg(img, newToken) }
                }
                is Result.Error -> {
                    mutableErrorMessage.value = "${result.code} ${result.message}"
                    Log.d(LLog.TAG, "GetUser Second API ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "GetUser Second ERROR -> ${result.e.message}")
                    mutableErrorMessage.value = "${result.e.message}"
                }
            }
        }
    }

    private fun initcalendar() {
        LLog.e("캘린더 조회 API")
        viewModelScope.launch(exceptionHandler) {
            isLoading.value = true

            when (val result = getFeelDataSource.invoke(token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "캘린더 조회 SUCCESS -> ${result.data} ")
                    val calendarSize = result.data?.calendarList?.size
                    if (calendarSize in 30..1000) {
                        ach3.postValue(R.drawable.ach_3)
                    } else {
                        ach3.postValue(R.drawable.ach_3)
                    }
                }
                is Result.Error -> {
                    Log.d(LLog.TAG, "캘린더 조회 ERROR -> ${result.message}")
                    mutableErrorMessage.value = "${result.code} ${result.message}"
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "캘린더 조회 ERROR -> ${result.e.message}")
                    mutableErrorMessage.value = "${result.e.message}"
                }
            }
        }
    }

    private fun initSendData() {
        LLog.e("의료데이터 판매 조회 API")
        viewModelScope.launch(exceptionHandler) {
            isLoading.value = true
            when (val result = getSendDataSource.invoke(token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "의료데이터 판매 조회 SUCCESS -> ${result.data} ")
                    val calendarSize = result.data?.datalist?.size
                    if (calendarSize in 10..1000) {
                        ach3.postValue(R.drawable.bch_2)
                    } else {
                        ach3.postValue(R.drawable.bch_2)
                    }
                }
                is Result.Error -> {
                    Log.d(LLog.TAG, "의료데이터 판매 조회 ERROR -> ${result.message}")
                    mutableErrorMessage.value = "${result.code} ${result.message}"
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "의료데이터 판매 조회 ERROR -> ${result.e.message}")
                    mutableErrorMessage.value = "${result.e.message}"
                }
            }
        }
    }

    private fun initDocument() {
        LLog.e("제휴병원 서류 조회 API")
        viewModelScope.launch(exceptionHandler) {
            isLoading.value = true
            when (val result = getDocumentDataSource.invoke(token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "제휴병원 서류 조회 SUCCESS -> ${result.data} ")
                    val calendarSize = result.data?.documentList?.size
                    if (calendarSize in 10..1000) {
                        ach3.postValue(R.drawable.bch_4)
                    } else {
                        ach3.postValue(R.drawable.bch_4)
                    }
                }
                is Result.Error -> {
                    Log.d(LLog.TAG, "제휴병원 서류 조회 ERROR -> ${result.code} ${result.message}")
                    mutableErrorMessage.value = "${result.code} ${result.message}"
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "제휴병원 서류 조회 ERROR -> ${result.e.message}")
                    mutableErrorMessage.value = "${result.e.message}"
                }
            }
        }
    }

    private fun profileImg(profile: String?, newToken: String) {
        LLog.e("프로필 이미지 API")
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            when (val result = getProfileImageDataSource.invoke(profile ?: "", token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "getProfileImg  response SUCCESS -> ${result.data}")
                    val imgs = result.data?.byteStream()
                    val bit = BitmapFactory.decodeStream(imgs)
                    achProfileImg.postValue(bit)
                }
                is Result.Error -> {
                    Log.d(
                        LLog.TAG,
                        "getProfileImg response ERROR -> ${result.code} ${result.message}"
                    )
                    otherImgAPI(profile, newToken)
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "getProfileImg Fail -> ${result.e.message}")
                    mutableErrorMessage.value = "${result.e.message}"
                }
            }
            isLoading.postValue(false)
        }
    }

    private fun otherImgAPI(profile: String?, newToken: String) {
        LLog.e("프로필 이미지_두번째 API")

        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            when (val result = getProfileImageDataSource.invoke(profile ?: "", newToken)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "getProfileImg second response SUCCESS -> $profile: ${result.data}")
                    val imgs = result.data?.byteStream()
                    val bit = BitmapFactory.decodeStream(imgs)
                    achProfileImg.postValue(bit)
                }
                is Result.Error -> {
                    Log.d(
                        LLog.TAG,
                        "getProfileImg second response ERROR -> ${result.code} ${result.message}"
                    )
                    mutableErrorMessage.value = "${result.code} ${result.message}"
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "getProfileImg second Fail -> ${result.e.message}")
                    mutableErrorMessage.value = "${result.e.message}"
                }
            }
            isLoading.postValue(false)
        }
    }
}