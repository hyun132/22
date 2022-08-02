package com.iium.iium_medioz.util.keyword

import androidx.lifecycle.LiveData
import com.iium.iium_medioz.model.upload.KeywordEntity

interface KeywordRepository {
    suspend fun deleteKeyword(keyword: String)
    fun readKeywordList(): LiveData<List<KeywordEntity>>
    suspend fun writeKeyword(keyword: String): Result<Any>
}