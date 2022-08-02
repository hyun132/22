package com.iium.iium_medioz.util.keyword

import javax.inject.Inject

class WriteKeywordUseCase
constructor(
    private val keywordRepository: KeywordRepository
) {
    suspend operator fun invoke(keyword: String)
            = keywordRepository.writeKeyword(keyword)
}