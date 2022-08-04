package com.iium.iium_medioz.util.keyword

import androidx.lifecycle.LiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class ReadKeywordListUseCase @Inject constructor(
    private val keywordRepository: KeywordRepository
) {
    operator fun invoke(): LiveData<List<KeywordEntity>>
            = keywordRepository.readKeywordList()
}