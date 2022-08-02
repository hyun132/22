package com.iium.iium_medioz.viewmodel.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataViewModel : ViewModel() {

    var numClothes = mutableListOf<MutableLiveData<Int>>(MutableLiveData(0), MutableLiveData(0), MutableLiveData(0), MutableLiveData(0))

    private val _choicedClothesTabIndex = MutableLiveData(0)
    val choicedClothesTabIndex: LiveData<Int> = _choicedClothesTabIndex

}