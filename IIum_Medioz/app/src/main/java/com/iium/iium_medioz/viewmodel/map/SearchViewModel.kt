package com.iium.iium_medioz.viewmodel.map

import android.app.Application
import android.content.res.AssetManager
import android.util.Log
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import com.iium.iium_medioz.api.RetrofitBuilder
import com.iium.iium_medioz.model.map.KaKaoModel
import com.iium.iium_medioz.model.map.MapMarker
import com.iium.iium_medioz.util.base.MyApplication.Companion.prefs
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
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
        .asFlow() // BroadcastChannel을 hot flow로 바꿈
        .debounce(SEARCH_TIMEOUT) // search() 호출 속도를 조절할 수 있음. 해당 ms동안 새로운 텍스트를 입력하지 않으면 search() 호출
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
                    Log.e("에러", "_status false")
                    _status.postValue(false)
                }
            }
        }
        .catch { e: Throwable ->
            Log.e("에러", "에러 핸들링")
            e.printStackTrace()
        }
        .asLiveData()
}