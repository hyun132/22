package com.iium.iium_medioz.viewmodel.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.R
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.datasource.GetMedicalDataSource
import com.iium.iium_medioz.model.datasource.GetUserDataUseCase
import com.iium.iium_medioz.util.base.SingleLiveEvent
import com.iium.iium_medioz.util.log.LLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch

/**
 * 기존코드
 */


@ExperimentalCoroutinesApi
@FlowPreview
class HomeViewModel(
    private val remoteUserDataSource: GetUserDataUseCase,
    private val getMedicalDataSource: GetMedicalDataSource,
    private val token: String,
) : ViewModel() {

    val mutableErrorMessage = SingleLiveEvent<String>()
    private var _nickName = MutableLiveData<String>("")
    val nickname: LiveData<String>
        get() = _nickName
    val isLoading = MutableLiveData<Boolean>(false)
    val imgDataTem = MutableLiveData<Int>()
    val tvLabel = MutableLiveData<String>()
    val tvTem = MutableLiveData<String>()
    val _bannerImageId = MutableLiveData<Int>(R.drawable.main_first)
    val bannerImageId: LiveData<Int> = _bannerImageId
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        mutableErrorMessage.value = "Exception handled: ${throwable.localizedMessage}"
    }

    init {
        getUser()
        initView()
        initList()
    }

    fun initView() {            // 메인 상단 Background 변경
        val images = intArrayOf(
            R.drawable.main_first,
            R.drawable.main_second,
            R.drawable.main_third,
            R.drawable.main_banner_four
        )
        val imageId = (Math.random() * images.size).toInt()
        _bannerImageId.value = images[imageId]
    }

    fun getUser() {
        Log.d(LLog.TAG, "HomeViewModel getUser()")
        viewModelScope.launch(exceptionHandler) {
            isLoading.value = true
            when (val result = remoteUserDataSource.invoke(token)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "userName is ${result.data.user?.name}")
                    _nickName.postValue(result.data.user?.name?:"홍길동")
                }
                is Result.Error -> mutableErrorMessage.value =
                    "${result.code} ${result.message}"
                is Result.Exception -> Log.d(LLog.TAG, "${result.e.message}")
            }
        }
    }

    private fun initData(allscore: List<String?>?) {
        val all: List<Int>? = allscore?.map { it!!.toInt() }
        val allitem = all?.reduce { result, item -> result + item }
        val allsize = all?.count()
        val all_score = allitem!! / allsize!!

        tvTem.value = all_score.toString()
        tvLabel.value = (allsize / 10 + 1).toString()
        this.imgDataTem.value = when {
            (all_score in 0..19) -> R.drawable.main_tem_first
            (all_score in 20..39) -> R.drawable.main_tem_second
            (all_score in 40..59) -> R.drawable.main_tem_third
            (all_score in 60..79) -> R.drawable.main_tem_four
            (all_score in 80..100) -> R.drawable.main_tem_five
            else -> R.drawable.main_tem_first
        }

    }
    private fun initList() {
        LLog.e("데이터 조회_두번째 API")
        if (token.isEmpty()) return //이땐 어떤 처리 ??
        viewModelScope.launch(exceptionHandler) {
            isLoading.value = true
            when (val result = getMedicalDataSource.invoke(token)) {
                is Result.Success -> {
                    if (result.data.datalist.isNullOrEmpty()) {
                        Log.d(LLog.TAG, "데이터가 없습니다.")
                    } else initData(result.data.datalist.map { list -> list.allscore })
                }
                is Result.Error -> mutableErrorMessage.value =
                    "${result.code} ${result.message}"
                is Result.Exception -> Log.d(LLog.TAG, "${result.e.message}")
            }
            isLoading.value = false
        }
    }
}