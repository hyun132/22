package com.iium.iium_medioz.viewmodel.calendar

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.iium.iium_medioz.model.calendar.Jurnal
import com.iium.iium_medioz.util.feel.ResultState
import com.iium.iium_medioz.util.firebase.FirebaseService


class SearchViewModel : ViewModel() {
    private val resultStateSearch = MutableLiveData<ResultState>()
    val resultStateDeleteJurnal = MutableLiveData<ResultState>()

    fun searchText(deviceId: String) {
        resultStateSearch.value = ResultState.Loading(true)
        FirebaseService.getText(deviceId)
            .get()
            .addOnSuccessListener {
                resultStateSearch.value = ResultState.Success(it)
            }.addOnFailureListener {
                FirebaseCrashlytics.getInstance().recordException(it)
                resultStateSearch.value = ResultState.Error(it)
                resultStateSearch.value = ResultState.Loading(false)
            }
    }

    fun deleteJurnal(jurnal: Jurnal) {
        FirebaseService.deleteData(jurnal)
            .addOnFailureListener {
                FirebaseCrashlytics.getInstance().recordException(it)
                resultStateDeleteJurnal.value = ResultState.Error(it)
            }.addOnSuccessListener {
                resultStateDeleteJurnal.value = ResultState.Success(it)
            }
    }
}