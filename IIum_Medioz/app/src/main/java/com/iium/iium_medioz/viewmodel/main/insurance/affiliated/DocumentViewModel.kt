package com.iium.iium_medioz.viewmodel.main.insurance.affiliated

import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.datasource.PostDocumentDataSource
import com.iium.iium_medioz.model.document.DocumentModel
import com.iium.iium_medioz.util.base.SingleLiveEvent
import com.iium.iium_medioz.util.log.LLog
import com.iium.iium_medioz.view.main.bottom.data.SaveActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class DocumentViewModel(
    private val postDocumentDataSource: PostDocumentDataSource,
    private val newToken: String
) : ViewModel() {
    val mutableErrorMessage = SingleLiveEvent<String>()
    val onlyDate = MutableLiveData(LocalDate.now().toString())
    val name = MutableLiveData("")
    val address = MutableLiveData("")
    val address_city = MutableLiveData("대전")
    val address_district = MutableLiveData("서구")
    val address_location = MutableLiveData("월평동")
    val call = MutableLiveData("")
    val username = MutableLiveData("")
    val userFirstNumber = MutableLiveData("")
    val userSecondNumber = MutableLiveData("")
    val usercall = MutableLiveData("")
    val userreqdocument = MutableLiveData("")
    val timestamp = MutableLiveData("")
    val isLoading = MutableLiveData<Boolean>(false)
    val inquiry_first = MutableLiveData("")
    val inquiry_second = MutableLiveData("")
    val inquiry_document = MutableLiveData("")

    private var imgUrl:String? =""

    fun initAPI() {
        val data = DocumentModel(
            name.value,
            address.value,
            address_city.value,
            address_district.value,
            address_location.value,
            call.value,
            username.value,
            "${userFirstNumber.value}-${userSecondNumber.value}",
            usercall.value,
            userreqdocument.value,
            imgUrl,
            inquiry_first.value,
            inquiry_second.value,
            inquiry_document.value,
            timestamp.value
        )

        LLog.e("제휴병원 서류신청")
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.value = true
            when (val result = postDocumentDataSource.invoke(newToken, data)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "postDocument  API SUCCESS -> $result")
//                    moveSave()
                }
                is Result.Error -> {
                    mutableErrorMessage.value = "${result.code} ${result.message}"
                    Log.d(LLog.TAG, "postDocument  API ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    mutableErrorMessage.value = result.e.localizedMessage
                    Log.d(LLog.TAG, "postDocument  Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }



    fun setViewData(doname:String?,doAddress:String?,doCall:String?,imgUrl:String?){
        name.postValue(doname)
        address.postValue(doAddress)
        call.postValue(doCall)
        this.imgUrl = imgUrl
    }
}