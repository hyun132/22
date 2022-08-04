package com.iium.iium_medioz.viewmodel.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.util.base.SingleLiveEvent
import com.iium.iium_medioz.util.keyword.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KeyWordViewModel@Inject constructor(@ApplicationContext
    private val deleteKeywordUseCase: DeleteKeywordUseCase,
    private val readKeywordListUseCase: ReadKeywordListUseCase,
    private val writeKeywordUseCase: WriteKeywordUseCase,
    private val hasSearchResultUseCase: HasSearchResultUseCase
): ViewModel() {
    private val keywordList: LiveData<List<KeywordEntity>> = readKeywordListUseCase.invoke()

    private val _searchResult = SingleLiveEvent<Boolean>()
    val searchResult: LiveData<Boolean> get() = _searchResult

    private val _problem = MutableLiveData<Result<Any>>()
    val problem: LiveData<Result<Any>> get() = _problem

    fun deleteKeyword(keyword: String) {
        viewModelScope.launch {
            try {
                deleteKeywordUseCase.invoke(keyword)
            } catch (e: Exception) {
                _problem.postValue(Result(Status.FAIL, null, null))
            }
        }
    }

    fun readKeywordList(): LiveData<List<KeywordEntity>> {
        return keywordList
    }

    fun writeKeyword(keyword: String) {
        viewModelScope.launch {
            val result = writeKeywordUseCase.invoke(keyword)
            if(result.status == Status.ERROR) {
                _problem.postValue(result)
            }
        }
    }

    fun getSearchResult(keyword: String) {
        viewModelScope.launch {
            val result = hasSearchResultUseCase(keyword)
            if(result.status == Status.SUCCESS) {
                result.data.let { _searchResult.postValue(it) }
            } else {
                _problem.postValue(result)
            }
        }
    }
}