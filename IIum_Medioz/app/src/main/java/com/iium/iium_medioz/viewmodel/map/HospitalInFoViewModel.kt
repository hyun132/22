package com.iium.iium_medioz.viewmodel.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iium.iium_medioz.model.map.BannerItem

class HospitalInFoViewModel : ViewModel() {

    private val _bannerItemList: MutableLiveData<List<BannerItem>> = MutableLiveData()

    val bannerItemList: LiveData<List<BannerItem>>
        get() = _bannerItemList


    fun setBannerItems(list: List<BannerItem>){
        _bannerItemList.value = list
    }

}