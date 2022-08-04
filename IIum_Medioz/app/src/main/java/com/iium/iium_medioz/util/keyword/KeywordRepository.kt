package com.iium.iium_medioz.util.keyword

import androidx.lifecycle.LiveData
import dagger.Module


interface KeywordRepository {
    suspend fun deleteKeyword(keyword: String)
    fun readKeywordList(): LiveData<List<KeywordEntity>>
    suspend fun writeKeyword(keyword: String): Result<Any>
}