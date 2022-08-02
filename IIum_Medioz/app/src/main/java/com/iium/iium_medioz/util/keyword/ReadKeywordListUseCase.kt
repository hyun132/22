package com.iium.iium_medioz.util.keyword

import androidx.lifecycle.LiveData
import com.iium.iium_medioz.model.upload.KeywordEntity
import javax.inject.Inject

class ReadKeywordListUseCase
 constructor(
    private val keywordRepository: KeywordRepository
) {
    operator fun invoke(): LiveData<List<KeywordEntity>>
            = keywordRepository.readKeywordList()
}