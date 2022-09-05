package com.iium.iium_medioz.di

import com.iium.iium_medioz.model.di.MainRepository
import com.iium.iium_medioz.viewmodel.map.MapSearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
val module = module {
    factory {
        MainRepository()
    }
    viewModel {
        MapSearchViewModel(get(), get())
    }
}