package com.kesicollection.data.usecase

import com.kesicollection.core.model.Discover
import com.kesicollection.data.repository.DiscoverRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Use case for retrieving discover content.
 *
 * This class encapsulates the logic for fetching discover content from the [DiscoverRepository].
 * It provides a simple `invoke` operator function to execute the use case.
 *
 * @property discoverRepository The repository responsible for fetching discover content.
 */
@Singleton
class GetDiscoverContentUseCase @Inject constructor(
    private val discoverRepository: DiscoverRepository
) {
    suspend operator fun invoke(): Result<Discover> =
        discoverRepository.getDiscoverContent()
}