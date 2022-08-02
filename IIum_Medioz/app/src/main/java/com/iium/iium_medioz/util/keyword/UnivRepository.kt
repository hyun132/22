package com.iium.iium_medioz.util.keyword

import androidx.paging.PagingData
import com.iium.iium_medioz.model.Univ
import kotlinx.coroutines.flow.Flow

interface UnivRepository {
    fun getUnivNoticeList(categoryId: String?): Flow<PagingData<Univ>>
    suspend fun getRecentUnivNoticeList(): Result<List<Univ>>
    suspend fun hasSearchResult(keyword: String): Result<Boolean>
    fun getSearchResultList(keyword: String): Flow<PagingData<Univ>>
}