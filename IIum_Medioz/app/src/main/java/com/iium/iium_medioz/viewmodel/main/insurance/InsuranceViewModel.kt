package com.iium.iium_medioz.viewmodel.main.insurance

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.datasource.GetDocumentDataUseCase
import com.iium.iium_medioz.model.document.DocumentList
import com.iium.iium_medioz.util.base.SingleLiveEvent
import com.iium.iium_medioz.util.log.LLog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InsuranceViewModel(
    private val getDocumentDataSource: GetDocumentDataUseCase,
    private val newToken: String
) : ViewModel() {

    val mutableErrorMessage = SingleLiveEvent<String>()

    val isLoading = MutableLiveData<Boolean>(false)
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        mutableErrorMessage.value = "Exception handled: ${throwable.localizedMessage}"
    }

    val documentList = MutableLiveData<List<DocumentList>>()

    init {
        initView()
    }

    private fun initView() {
        LLog.e("서류신청 조회 API")
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            when (val result = getDocumentDataSource.invoke(newToken)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "서류신청 조회 SUCCESS -> ${result.data}")
                    documentList.postValue(result.data?.documentList)
                }
                is Result.Error -> {
                    mutableErrorMessage.value = "${result.code} ${result.message}"
                    Log.d(LLog.TAG, "서류신청 조회 response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "서류신청 조회 Fail -> ${result.e.message}")
                    mutableErrorMessage.value = "${result.e.message}"
                }
            }
        }
    }
}