package com.iium.iium_medioz.util.keyword

import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class HasSearchResultUseCase @Inject constructor(
    private val univRepository: UnivRepository
) {
    suspend operator fun invoke(keyword: String): Result<Boolean> = univRepository.hasSearchResult(keyword)
}