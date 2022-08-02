package com.iium.iium_medioz.util.keyword

import javax.inject.Inject

class HasSearchResultUseCase
 constructor(
    private val univRepository: UnivRepository
) {
    suspend operator fun invoke(keyword: String): Result<Boolean> = univRepository.hasSearchResult(keyword)
}