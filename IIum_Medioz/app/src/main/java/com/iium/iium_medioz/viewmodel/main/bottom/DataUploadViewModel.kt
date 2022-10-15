package com.iium.iium_medioz.viewmodel.main.bottom

import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.iium.iium_medioz.model.datasource.Result
import com.iium.iium_medioz.model.datasource.GetCreateUseCase
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.base.SingleLiveEvent
import com.iium.iium_medioz.util.common.Event
import com.iium.iium_medioz.util.log.LLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class DataUploadViewModel(
    private val getCreateUseCase: GetCreateUseCase,
    private val newToken: String
) : ViewModel() {

    var files4: MutableList<Uri> = ArrayList()      // 텍스트 이미지
    var files5: MutableList<Uri> = ArrayList()      // 일반 이미지
    var files6: MutableList<Uri> = ArrayList()      // 비디오

    val registeredKeywordCount = MutableLiveData<String>("0")
    val count: LiveData<String> get() = liveData { (files4.size ?: 0).toString() }
    val _normalCount = MutableLiveData<String>("0")
    val normalCount: LiveData<String> get() = liveData { (files5.size ?: 0).toString() }
    val _vedioCount = MutableLiveData<String>("0")
    val vedioCount: LiveData<String> get() = liveData { (files6.size ?: 0).toString() }
    val sendcode = Constant.SEND_CODE_FALSE
    val defaultcode = Constant.DEFAULT_CODE_TRUE
    val sensitivity = ""
    val requestHashMap: HashMap<String, RequestBody> = HashMap()
    val mutableErrorMessage = SingleLiveEvent<String>()
    val timestamp = MutableLiveData("0")

    private var _title = MutableLiveData<String>("")
    val title get() = _title
    private var _titleError = MutableLiveData<String>("")
    val titleError get() = _titleError
    val myKeyword = MutableLiveData<String>("")

    private val _registeredKeyword: MutableLiveData<ArrayList<String>> = MutableLiveData(arrayListOf<String>())
    val registeredKeyword :LiveData<ArrayList<String>> get() = _registeredKeyword

    private val _viewEvent = MutableLiveData<Event<Any>>()
    val viewEvent: LiveData<Event<Any>>
        get() = _viewEvent

    fun viewEvent(content: Any) {
        _viewEvent.postValue(Event(content))
    }

    fun setViewData(onlyDate: String) {
        timestamp.value = onlyDate
    }

    /////////////////// API 호출 ///////////////////
    fun callCreateAPI() {
        val textimg: MutableList<MultipartBody.Part?> = ArrayList()
        for (uri: Uri in files4) {
            uri.path?.let {
                Log.i("textImg", it)
            }
            textimg.add(prepareFilePart("textImg", uri))
            Log.e("textImg", uri.toString())
        }

        for (imguri: Uri in files5) {
            imguri.path?.let {
                Log.i("Img", it)
            }
            textimg.add(prepareFilePart("Img", imguri))
            Log.e("Img", imguri.toString())
        }

        for (videouri: Uri in files6) {
            videouri.path?.let {
                Log.i("video", it)
            }
            textimg.add(prepareFilePart("video", videouri))
            Log.e("video", videouri.toString())
        }

        val picksize = files4.size
        val pick = 0
        val pickkk = when (picksize) {
            1 -> pick + 4
            2 -> pick + 8
            3 -> pick + 12
            4 -> pick + 16
            5 -> pick + 20
            else -> pick + 0
        }
        val pickscore = pickkk.toString()

        val videosize = files6.size
        val video = 0
        val videooo = when (videosize) {
            1 -> video + 4
            2 -> video + 8
            3 -> video + 12
            4 -> video + 16
            5 -> video + 20
            else -> video + 0
        }
        val videoscore = videooo.toString()

        val keyscore = 0
        val key = _registeredKeyword.value?.count()
        val keyy = when (key) {
            1 -> keyscore + 4
            2 -> keyscore + 8
            3 -> keyscore + 12
            4 -> keyscore + 16
            5 -> keyscore + 20
            else -> keyscore + 0
        }
        val keywordscore = keyy.toString()

        val pickk: Int = pickscore.toInt()
        val vidd: Int = videoscore.toInt()
        val kkk: Int = keywordscore.toInt()

        val all = pickk + vidd + kkk
        val allscore: String = all.toString()
        val title = title.value ?: ""
        val keyword = this.registeredKeyword.value.toString() ?: ""
        val timestamps = timestamp.value ?: ""

        requestHashMap["title"] = title.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["keyword"] = keyword.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["timestamp"] =
            timestamps.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sendcode"] =
            sendcode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["defaultcode"] =
            defaultcode.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["sensitivity"] =
            sensitivity.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["pickscore"] =
            pickscore.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["videoscore"] =
            videoscore.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["keywordscore"] =
            keywordscore.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["allscore"] =
            allscore.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        LLog.d("데이터 업로드_두번째 API")
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getCreateUseCase.invoke(newToken, textimg, requestHashMap)) {
                is Result.Success -> {
                    Log.d(LLog.TAG, "getCreate Second API SUCCESS -> ${result.data}")
                    viewEvent(NAVIGATE_SAVE_ACTIVITY)
                }
                is Result.Error -> {
                    mutableErrorMessage.postValue("${result.code} ${result.message}")
                    Log.d(LLog.TAG, "getCreate Second API ERROR -> ${result.message}")
                }
                is Result.Exception -> {
                    mutableErrorMessage.postValue(result.e.localizedMessage)
                    Log.d(LLog.TAG, "getCreate Second Fail -> ${result.e.localizedMessage}")
                }
            }
        }
    }

    fun createKeyWord() {
        val keyword = myKeyword.value
        LLog.d(keyword)
        if (keyword.isNullOrEmpty()) return
        if(_registeredKeyword.value == null) _registeredKeyword.value = arrayListOf<String>()
        _registeredKeyword.value!!.add(keyword)
        val size = _registeredKeyword.value?.count() ?: 0
        if (size == 6) {
            mutableErrorMessage.postValue("키워드는 5개만 등록할 수 있습니다.")
            _registeredKeyword.value?.clear()
            viewEvent(NOTIFY_DATA_DELETED)
        } else {
            registeredKeywordCount.postValue(size.toString())
            viewEvent(NOTIFY_DATA_ADDED)
        }
        this.myKeyword.postValue("")

    }

    fun deleteText(keyword:String?) {
        if (keyword.isNullOrEmpty()) {
            mutableErrorMessage.postValue("키워드를 등록해주세요")
        } else {
            _registeredKeyword.value?.remove(keyword)
            val size = _registeredKeyword.value?.count() ?: 0
            registeredKeywordCount.postValue(size.toString())
        }
//        viewEvent(NOTIFY_DATA_CHANGED)
    }

    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path!!)
        Log.i("here is error", file.absolutePath.toString())
        val requestFile: RequestBody = file
            .asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    companion object {
        const val NAVIGATE_SAVE_ACTIVITY = 11111
        const val SHOW_ERROR_DIALOG = 20000
        const val NOTIFY_DATA_DELETED = 30000
        const val NOTIFY_DATA_ADDED = 40000

    }

}