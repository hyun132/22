package com.iium.iium_medioz.di

import com.iium.iium_medioz.model.di.MainRepository
import com.iium.iium_medioz.viewmodel.map.MapSearchViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    factory {
        MainRepository()
    }
    viewModel {
        MapSearchViewModel(get(), get())
    }
}