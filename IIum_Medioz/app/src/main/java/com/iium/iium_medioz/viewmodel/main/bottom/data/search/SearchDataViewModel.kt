package com.iium.iium_medioz.viewmodel.main.bottom.data.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.datasource.GetSearchDataSource
import com.iium.iium_medioz.model.rest.base.CreateName
import com.iium.iium_medioz.model.rest.base.DataList
import com.iium.iium_medioz.util.common.Event
import com.iium.iium_medioz.util.log.LLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchDataViewModel(
    private val getSearchDataSource: GetSearchDataSource,
    private val myToken: String,
    private val newToken: String
) : ViewModel() {

    val searchKeyword = MutableLiveData<String>()
    val error = MutableLiveData<String>()
    val dataList = MutableLiveData<List<DataList>>()

    private val _viewEvent = MutableLiveData<Event<Any>>()
    val viewEvent: LiveData<Event<Any>> get() = _viewEvent

    private fun viewEvent(content: Any) {
        _viewEvent.postValue(Event(content))
    }

    private val _myDataList = MutableLiveData<List<DataList>>()
    val myDataList: LiveData<List<DataList>> get() = _myDataList


    fun initAPI() {
//        error.value=""
        val name = searchKeyword.value
        if (name.isNullOrEmpty()) {
//            error.value="미입력"
            return
        }
        LLog.e("검색_첫번째 API")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getSearchDataSource.invoke(myToken, name)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "Search response SUCCESS -> ${result.data}")
                    result.data.result.let { dataList.postValue(it) }
                }
                is Result.Error -> {
                    Log.d(LLog.TAG, "Search response ERROR ->  ${result.message}")
                    otherAPI()
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "Search Fail -> $ ${result.e.localizedMessage}")
                    viewEvent(SHOW_ERROR_DIALOG)
                }
            }
        }
    }

    private fun otherAPI() {
        LLog.e("검색_두번째 API")
        val name = searchKeyword.value.toString()
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getSearchDataSource.invoke(newToken, name)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "Search Second response SUCCESS -> ${result.data}")
                    result.data.result.let { dataList.postValue(it) }


                }
                is Result.Error -> {
                    Log.d(LLog.TAG, "Search Second response ERROR -> ${result.message}")
                    viewEvent(SHOW_ERROR_DIALOG)
                }
                is Result.Exception -> {
                    Log.d(LLog.TAG, "Search Second Fail -> ${result.e.localizedMessage}")
                    viewEvent(SHOW_ERROR_DIALOG)
                }
            }
        }

    }



    companion object {
        const val NAVIGATE_SAVE_SEND_ACTIVITY = 33333
        const val SHOW_ERROR_DIALOG = 55555
    }
}