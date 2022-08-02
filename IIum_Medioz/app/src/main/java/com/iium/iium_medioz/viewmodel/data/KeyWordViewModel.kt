package com.iium.iium_medioz.viewmodel.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iium.iium_medioz.model.upload.KeywordEntity
import com.iium.iium_medioz.util.base.SingleLiveEvent
import com.iium.iium_medioz.util.keyword.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class KeyWordViewModel : ViewModel() {

    private val _searchResult = SingleLiveEvent<Boolean>()
    val searchResult: LiveData<Boolean> get() = _searchResult

    private val _problem = MutableLiveData<Result<Any>>()
    val problem: LiveData<Result<Any>> get() = _problem

    fun deleteKeyword(keyword: String) {

    }


    fun writeKeyword(keyword: String) {

    }

    fun getSearchResult(keyword: String) {

    }
}