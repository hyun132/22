package com.iium.iium_medioz.util.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.iium.iium_medioz.model.network.MyState
import kotlinx.coroutines.Dispatchers

class NetworkStatusViewModel(
    networkStatusTracker: NetworkStatusTracker,
) : ViewModel() {

    val state =
        networkStatusTracker.networkStatus
            .map(
                onUnavailable = { MyState.Error },
                onAvailable = { MyState.Fetched },
            )
            .asLiveData(Dispatchers.IO)
}