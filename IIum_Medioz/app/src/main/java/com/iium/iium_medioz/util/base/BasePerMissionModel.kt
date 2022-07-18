package com.iium.iium_medioz.util.base

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BasePerMissionModel (application: Application) : AndroidViewModel(application) {

    @SuppressLint("StaticFieldLeak")
    protected val context = getApplication<Application>().applicationContext
    private val disposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()

        // clear all the subscription
        disposable.clear()
    }

}