package com.iium.iium_medioz.util.keyword

import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class WriteKeywordUseCase
@Inject constructor(
    private val keywordRepository: KeywordRepository
) {

    suspend operator fun invoke(keyword: String)
            = keywordRepository.writeKeyword(keyword)
}