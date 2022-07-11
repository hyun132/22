package com.iium.iium_medioz.util.feel

sealed class ResultState {
    class Success<T>(val data: T) : ResultState()
    class Error(val e: Throwable) : ResultState()
    class Loading(val loading: Boolean) : ResultState()
}