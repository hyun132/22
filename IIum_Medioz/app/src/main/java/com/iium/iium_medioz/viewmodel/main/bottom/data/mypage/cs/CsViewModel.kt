package com.iium.iium_medioz.viewmodel.main.bottom.data.mypage.cs

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.datasource.GetCounRequestDataUseCase
import com.iium.iium_medioz.model.ui.CounList
import com.iium.iium_medioz.util.log.LLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CsViewModel(
    private val getCounRequestDataSource: GetCounRequestDataUseCase,
    private val myToken: String,
    private val newToken: String
) : ViewModel() {

    private val _counList = MutableLiveData<List<CounList>>()
    val counList: LiveData<List<CounList>> get() = _counList

    init {
        initAPI()
    }

    private fun initAPI() {
        LLog.e("1:1문의_첫번째 API")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getCounRequestDataSource.invoke(myToken)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "CounGet response SUCCESS -> ${result.data}")
                    _counList.postValue(result.data.userRequest?: emptyList())
                }
                is Result.Error -> {
                    otherAPI()
                    Log.d(LLog.TAG, "CounGet response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "CounGet Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    private fun otherAPI() {
        LLog.e("1:1문의_두번째 API")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getCounRequestDataSource.invoke(newToken)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "CounGet Second response SUCCESS -> ${result.data}")
                    _counList.postValue(result.data.userRequest?: emptyList())
                }
                is Result.Error -> {
                    otherAPI()
                    Log.d(LLog.TAG, "CounGet Second response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "CounGet Second Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }
}