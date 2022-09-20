package com.iium.iium_medioz.model.network

sealed class MyState {
    object Fetched : MyState()
    object Error : MyState()
}