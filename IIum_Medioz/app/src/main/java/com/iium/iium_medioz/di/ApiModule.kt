package com.iium.iium_medioz.di


import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.BINARY

@Qualifier
@Retention(BINARY)
annotation class Api


class ApiModule {
}

