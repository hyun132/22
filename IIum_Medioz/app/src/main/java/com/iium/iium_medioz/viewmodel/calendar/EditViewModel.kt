package com.iium.iium_medioz.viewmodel.calendar

import androidx.lifecycle.MutableLiveData
import com.iium.iium_medioz.model.calendar.SendFeelModel
import com.iium.iium_medioz.util.base.BaseViewModel
import com.iium.iium_medioz.util.feel.ResultState
import com.iium.iium_medioz.util.firebase.FirebaseService
import java.util.*

class EditViewModel : BaseViewModel() {
    val resultStatusSaveJurnal = MutableLiveData<ResultState>()
    val resultStateDeleteJurnal = MutableLiveData<ResultState>()

    fun saveJurnal(jurnal: SendFeelModel) {
        resultStatusSaveJurnal.value = ResultState.Loading(true)
        saveText(jurnal)
    }

    fun editJurnal(jurnal: SendFeelModel) {
        resultStatusSaveJurnal.value = ResultState.Loading(true)
        editText(jurnal)

    }

    private fun saveText(jurnal: SendFeelModel) {
        FirebaseService.saveText(jurnal)
            .addOnFailureListener {
                it.printStackTrace()
                resultStatusSaveJurnal.value = ResultState.Error(it)
                resultStatusSaveJurnal.value = ResultState.Loading(false)
            }.addOnSuccessListener {
                resultStatusSaveJurnal.value = ResultState.Success(it)
                resultStatusSaveJurnal.value = ResultState.Loading(false)
            }
    }

    private fun editText(jurnal: SendFeelModel) {
        FirebaseService.editText(jurnal)
            .addOnFailureListener {
                it.printStackTrace()
                resultStatusSaveJurnal.value = ResultState.Error(it)
                resultStatusSaveJurnal.value = ResultState.Loading(false)
            }.addOnSuccessListener {
                resultStatusSaveJurnal.value = ResultState.Success(it)
                resultStatusSaveJurnal.value = ResultState.Loading(false)
            }
    }

}