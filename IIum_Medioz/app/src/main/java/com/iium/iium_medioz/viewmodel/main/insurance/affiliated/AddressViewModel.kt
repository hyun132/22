package com.iium.iium_medioz.viewmodel.main.insurance.affiliated

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.datasource.GetMapDataSource
import com.iium.iium_medioz.model.map.AddressDocument
import com.iium.iium_medioz.util.base.SingleLiveEvent
import com.iium.iium_medioz.util.common.Event
import com.iium.iium_medioz.util.log.LLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddressViewModel(
    private val getMapDataSource: GetMapDataSource,
    private val newToken: String
) : ViewModel() {
    val mutableErrorMessage = SingleLiveEvent<String>()
    private val _addressList = MutableLiveData<List<AddressDocument>>()
    val addressList: LiveData<List<AddressDocument>> get() = _addressList
    private val _viewEvent = MutableLiveData<Event<Any>>()
    val viewEvent: LiveData<Event<Any>>
        get() = _viewEvent

    private fun viewEvent(content: Any) {
        _viewEvent.postValue(Event(content))
    }

    init {
        getAPI()
    }

    private fun getAPI() {
        LLog.e("제휴병원 좌표")
        val query = ""
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getMapDataSource.invoke(newToken, query)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "제휴병원 response SUCCESS -> ${result.data}")
                    result.let { dto ->
                        _addressList.postValue(dto.data.result)
                    }
                }
                is Result.Error -> {
                    viewEvent(SHOW_ERROR_DIALOG)
                    Log.d(LLog.TAG, "제휴병원 response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    viewEvent(SHOW_ERROR_DIALOG)
                    Log.d(LLog.TAG, "제휴병원 Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    companion object {
        const val NAVIGATE_MAIN_ACTIVITY = 33333
        const val SHOW_ERROR_DIALOG = 55555
    }
}