package com.iium.iium_medioz.util.keyword

import androidx.paging.PagingData
import dagger.Module
import kotlinx.coroutines.flow.Flow

interface UnivRepository {
    suspend fun hasSearchResult(keyword: String): Result<Boolean>
}