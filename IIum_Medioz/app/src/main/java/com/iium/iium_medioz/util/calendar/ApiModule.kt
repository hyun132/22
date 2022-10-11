package com.iium.iium_medioz.util.calendar

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Api

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideRetrofitProvider(uniqueId: UniqueIdentifier) = ApiFactory(uniqueId)

    @Provides
    @Singleton
    @Api
    fun provideAuth(provider: ApiFactory) = provider.createApi(AuthAPI::class)

    @Provides
    @Singleton
    @Api
    fun provideCalendar(provider: ApiFactory) = provider.createApi(CalendarAPI::class)

//    @Provides
//    @Singleton
//    @Api
//    fun provideClothes(provider: ApiFactory) = provider.createApi(ClothesAPI::class)
//
//    @Provides
//    @Singleton
//    @Api
//    fun provideUser(provider: ApiFactory) = provider.createApi(UserAPI::class)
//
//    @Provides
//    @Singleton
//    @Api
//    fun provideWeather(provider: ApiFactory) = provider.createApi(WeatherAPI::class)
//
//    @Provides
//    @Singleton
//    @Api
//    fun provideWeahy(provider: ApiFactory) = provider.createApi(WeathyAPI::class)
}