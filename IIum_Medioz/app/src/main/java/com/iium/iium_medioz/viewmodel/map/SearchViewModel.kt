package com.iium.iium_medioz.viewmodel.map

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.iium.iium_medioz.api.RetrofitBuilder
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import retrofit2.HttpException

@ExperimentalCoroutinesApi
@FlowPreview
class SearchViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val SEARCH_TIMEOUT = 300L
    @OptIn(ObsoleteCoroutinesApi::class)
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    private val _kakaoList = MutableLiveData<MapMarker>()
    val kakaoList: LiveData<MapMarker>
        get() = _kakaoList
    private val _status = MutableLiveData<Boolean>()
    val status: LiveData<Boolean>
        get() = _status

    @OptIn(ObsoleteCoroutinesApi::class)
    val searchResult = queryChannel
        .asFlow()
        .debounce(SEARCH_TIMEOUT)
        .mapLatest { text ->
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    _kakaoList.postValue(
                        RetrofitBuilder.api.getMap(
                            prefs.newaccesstoken,
                            text
                        )
                    )
                    _status.postValue(true)
                    Log.e("잘 들어옴", "_status true")
                } catch (e: HttpException) {
                    if(e is CancellationException){
                        throw e
                    }else{
                        Log.e("에러", "_status false")
                        _status.postValue(false)
                    }
                }
            }
        }
        .asLiveData()
}