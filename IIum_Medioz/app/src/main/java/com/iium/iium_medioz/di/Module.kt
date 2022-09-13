package com.iium.iium_medioz.di

import com.iium.iium_medioz.viewmodel.map.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val module = module {
    viewModel {
        SearchViewModel(get())
    }
}