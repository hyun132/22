package com.iium.iium_medioz.viewmodel.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.iium.iium_medioz.model.di.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
@FlowPreview
class MapSearchViewModel (application: Application,
                          repository: MainRepository
) : AndroidViewModel(application) {

    private val SEARCH_TIMEOUT = 300L
    /*
    Channel.CONFLATED는 ConflatedChannel를 생성
    ConflatedChannel:
        보내진 element 중에서 하나의 element만 버퍼링하므로서
        Receiver가 항상 최근에 보내진 element를 가져올 수 있도록함
     */
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    val searchResult = queryChannel
        .asFlow() // BroadcastChannel을 hot flow로 바꿈
        .debounce(SEARCH_TIMEOUT) // search() 호출 속도를 조절할 수 있음. 해당 ms동안 새로운 텍스트를 입력하지 않으면 search() 호출
        .mapLatest { text ->
            // 여기에 실제 Api를 호출하는 코드를 적어주시면 됩니다.
            withContext(Dispatchers.IO) {
                repository.search(text)
            }
        }
        .catch { e: Throwable ->
            // 에러 핸들링은 여기서!
            e.printStackTrace()
        }
        .asLiveData()
}