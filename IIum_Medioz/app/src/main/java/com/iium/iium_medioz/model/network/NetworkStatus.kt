package com.iium.iium_medioz.model.network

sealed class NetworkStatus {
    object Available : NetworkStatus()
    object Unavailable: NetworkStatus()
}