package com.iium.iium_medioz.viewmodel.main.bottom.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.datasource.GetCreateGetDataUseCase
import com.iium.iium_medioz.model.datasource.GetSendDataUseCase
import com.iium.iium_medioz.model.recycler.DataList
import com.iium.iium_medioz.model.send.SendList
import com.iium.iium_medioz.util.common.Event
import com.iium.iium_medioz.util.log.LLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * 얘는 DataFragment에 주입하며, 얘네는 AllFramgnet,SendFragment가 공유함.
 */

class DataViewModel(
    private val getSendDataSource: GetSendDataUseCase,
    private val getCreateGetDataSource: GetCreateGetDataUseCase,
    private val newToken: String
) : ViewModel() {

    private val _sendDataList = MutableLiveData<List<SendList>>()
    val sendDataList: LiveData<List<SendList>> get() = _sendDataList

    private val _myDataList = MutableLiveData<List<DataList>>()
    val myDataList: LiveData<List<DataList>> get() = _myDataList

    private val _viewEvent = MutableLiveData<Event<Any>>()
    val viewEvent: LiveData<Event<Any>>
        get() = _viewEvent

    private fun viewEvent(content: Any) {
        _viewEvent.postValue(Event(content))
    }

     fun initAllView() {
        LLog.e("데이터 조회_두번째 API")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getCreateGetDataSource.invoke(newToken)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "List Second response SUCCESS -> ${result.data} ")
                    _myDataList.postValue(result.data?.datalist ?: emptyList())
                }
                is Result.Error -> {
                    Log.d(LLog.TAG, "List Second response ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "List Second Fail -> ${result.e.message}")
                    viewEvent(SHOW_ERROR_DIALOG)
                }
            }
        }
    }

     fun initSendView() {
        LLog.e("판매 데이터 조회 API")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getSendDataSource.invoke(newToken)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "판매 데이터 조회 SUCCESS -> ${result.data} ")
                    _sendDataList.postValue(result.data?.datalist ?: emptyList())
                }
                is Result.Error -> {
                    Log.d(LLog.TAG, "판매 데이터 조회 ERROR -> ${result.message}")

                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "판매 데이터 조회 ERROR -> ${result.e.message}")
                    viewEvent(SHOW_ERROR_DIALOG)
                }
            }
        }
    }

    companion object {
        const val NAVIGATE_MAIN_ACTIVITY = 33333
        const val SHOW_ERROR_DIALOG = 55555
    }
}