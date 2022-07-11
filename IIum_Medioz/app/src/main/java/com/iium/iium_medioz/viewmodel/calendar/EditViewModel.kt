package com.iium.iium_medioz.viewmodel.calendar

import androidx.lifecycle.MutableLiveData
import com.iium.iium_medioz.model.calendar.SendFeelModel
import com.iium.iium_medioz.util.base.BaseViewModel
import com.iium.iium_medioz.util.feel.ResultState

class EditViewModel : BaseViewModel() {

    val resultStatusSaveJurnal = MutableLiveData<ResultState>()
    val resultStatusGetAudio = MutableLiveData<ResultState>()


}