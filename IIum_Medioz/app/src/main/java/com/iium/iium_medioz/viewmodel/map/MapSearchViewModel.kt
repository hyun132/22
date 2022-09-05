package com.iium.iium_medioz.viewmodel.map

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.google.gson.GsonBuilder
import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.NaverService
import com.iium.iium_medioz.model.di.MainRepository
import com.iium.iium_medioz.model.map.KaKaoLocalModel
import com.iium.iium_medioz.model.map.KaKaoModel
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.log.LLog
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.mapLatest
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@FlowPreview
class MapSearchViewModel (application: Application,
                          repository: MainRepository
) : AndroidViewModel(application) {
    private val SEARCH_TIMEOUT = 300L
    private val Authorization = "KakaoAK 82794c09f76246fe3780f1e6e2be4bfd"
    /*
    Channel.CONFLATED는 ConflatedChannel를 생성
    ConflatedChannel:
        보내진 element 중에서 하나의 element만 버퍼링하므로서
        Receiver가 항상 최근에 보내진 element를 가져올 수 있도록함
     */
    @OptIn(ObsoleteCoroutinesApi::class)
    val queryChannel = BroadcastChannel<String>(Channel.CONFLATED)

    @OptIn(ObsoleteCoroutinesApi::class)
    val searchResult = queryChannel
        .asFlow() // BroadcastChannel을 hot flow로 바꿈
        .debounce(SEARCH_TIMEOUT) // search() 호출 속도를 조절할 수 있음. 해당 ms동안 새로운 텍스트를 입력하지 않으면 search() 호출
        .mapLatest { text ->
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