package com.iium.iium_medioz.di

import com.iium.iium_medioz.api.APIService
import com.iium.iium_medioz.api.RetrofitClient
import com.iium.iium_medioz.model.datasource.*
import com.iium.iium_medioz.model.repository.DataRepository
import com.iium.iium_medioz.model.repository.DataRepositoryImpl
import com.iium.iium_medioz.util.`object`.Constant
import com.iium.iium_medioz.util.base.MyApplication
import com.iium.iium_medioz.viewmodel.main.AchievementViewModel
import com.iium.iium_medioz.viewmodel.main.bottom.DataModifyViewModel
import com.iium.iium_medioz.viewmodel.main.bottom.DataUploadViewModel
import com.iium.iium_medioz.viewmodel.main.bottom.DetailViewModel
import com.iium.iium_medioz.viewmodel.main.bottom.data.DataViewModel
import com.iium.iium_medioz.viewmodel.main.bottom.data.mypage.cs.CsViewModel
import com.iium.iium_medioz.viewmodel.main.bottom.data.search.SearchDataViewModel
import com.iium.iium_medioz.viewmodel.main.bottom.data.search.SearchDetailViewModel
import com.iium.iium_medioz.viewmodel.main.bottom.data.send.SendDetailViewModel
import com.iium.iium_medioz.viewmodel.main.bottom.data.send.SendViewModel
import com.iium.iium_medioz.viewmodel.main.home.HomeViewModel
import com.iium.iium_medioz.viewmodel.main.insurance.InsuranceViewModel
import com.iium.iium_medioz.viewmodel.main.insurance.affiliated.AddressViewModel
import com.iium.iium_medioz.viewmodel.main.insurance.affiliated.DocumentViewModel
import com.iium.iium_medioz.viewmodel.main.insurance.affiliated.HospitalViewModel
import com.iium.iium_medioz.viewmodel.map.SearchViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
val module = module {
    val myAccessToken = MyApplication.prefs.myaccesstoken ?: ""
    val newAccessToken = MyApplication.prefs.newaccesstoken ?: ""

    viewModel {
        AchievementViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            myAccessToken,
            newAccessToken
        )
    }
    viewModel { AddressViewModel(get(), newAccessToken) }
    viewModel { DocumentViewModel(get(), newAccessToken) }
    viewModel { DetailViewModel(get(), get(), newAccessToken) }
    viewModel { DataModifyViewModel(get(), get(), newAccessToken) }
    viewModel { DataUploadViewModel(get(), newAccessToken) }
    viewModel { HomeViewModel(get(), get(), myAccessToken) }
    viewModel { InsuranceViewModel(get(), newAccessToken) }
    viewModel { SearchViewModel(get()) }
    viewModel { SendViewModel(get(), get(), get(), newAccessToken) }
    viewModel { SendDetailViewModel(get(), get(), newAccessToken) }
    viewModel { SearchDetailViewModel(get(), get(), newAccessToken) }
    viewModel { SearchDataViewModel(get(), myAccessToken, newAccessToken) }
    viewModel { CsViewModel(get(), myAccessToken, newAccessToken) }
    viewModel { DataViewModel(get(),get(), newAccessToken) }
    viewModel { HospitalViewModel(get(),newAccessToken) }


    single<DataRepository> { DataRepositoryImpl(get()) }
    single<APIService> {
        RetrofitClient.getClient(Constant.BASE_URL).create(APIService::class.java)
    }

    factory { GetUserDataUseCase(get()) }
    factory { GetMedicalDataSource(get()) }
    factory { GetFeelDataUseCase(get()) }
    factory { GetDocumentDataUseCase(get()) }
    factory { GetProfileImageDataSource(get()) }
    factory { GetSendDataUseCase(get()) }
    factory { GetMapDataSource(get()) }
    factory { GetImgDataSource(get()) }
    factory { GetDataDeleteDataSource(get()) }
    factory { GetSendDeleteDataSource(get()) }
    factory { GetCreateUseCase(get()) }
    factory { GetSearchDataSource(get()) }
    factory { GetCounRequestDataUseCase(get()) }
    factory { GetCreateGetDataUseCase(get()) }
    factory { GetMapUseCase(get()) }

    factory { PostDocumentDataSource(get()) }
    factory { PostDataSendDataSource(get()) }
    factory { PostOCRDataSource(get()) }

    factory { PutModifyDataSource(get()) }
}