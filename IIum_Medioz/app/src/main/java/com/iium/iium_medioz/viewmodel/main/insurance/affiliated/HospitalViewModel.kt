package com.iium.iium_medioz.viewmodel.main.insurance.affiliated

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.datasource.GetMapUseCase
import com.iium.iium_medioz.model.map.AddressDocument
import com.iium.iium_medioz.util.common.Event
import com.iium.iium_medioz.util.log.LLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HospitalViewModel(
    private val GetMapUseCase: GetMapUseCase,
    private val newToken: String
) : ViewModel() {

    private val _locations = MutableLiveData<List<AddressDocument>>()
    val locations: LiveData<List<AddressDocument>> get() = _locations

    private val _viewEvent = MutableLiveData<Event<Any>>()
    val viewEvent: LiveData<Event<Any>>
        get() = _viewEvent

    private fun viewEvent(content: Any) {
        _viewEvent.postValue(Event(content))
    }

    private val _ismapReady = MutableLiveData<Boolean>(false)
    val ismapReady: LiveData<Boolean>
        get() = _ismapReady

    init {
        getAPI()
    }

    private fun getAPI() {
        LLog.e("제휴병원 좌표")
        val query = ""
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = GetMapUseCase.invoke(newToken, query)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "제휴병원 response SUCCESS -> ${result.data} ")
                    _locations.postValue(result.data?.result)
                }
                is Result.Error -> {
                    Log.d(LLog.TAG, "제휴병원 response ERROR -> ${result.message}")
                    viewEvent(SHOW_ERROR_DIALOG)
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "제휴병원 Fail -> ${result.e.localizedMessage}")
                    viewEvent(SHOW_ERROR_DIALOG)
                }
            }
        }
    }

    fun setIsMapReady(flag: Boolean){
        _ismapReady.value = flag
    }

    companion object {
        const val NAVIGATE_MAIN_ACTIVITY = 33333
        const val SHOW_ERROR_DIALOG = 55555
    }
}