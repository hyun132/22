package com.iium.iium_medioz.util.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel() : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _isLottieLoading = MutableLiveData(false)
    val isLottieLoading: LiveData<Boolean> get() = _isLottieLoading

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    fun Disposable.autoDispose() {
        compositeDisposable.add(this)
    }

    fun showProgress() {
        _isLoading.value = true
    }

    fun hideProgress() {
        _isLoading.value = false
    }

    fun showLottieProgress() {
        _isLottieLoading.value = true
    }

    fun hideLottieProgress() {
        _isLottieLoading.value = false
    }

}