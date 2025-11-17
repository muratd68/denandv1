package com.photoai.editor.domain.usecase

import com.photoai.editor.domain.model.FilterType
import com.photoai.editor.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Use case for checking if user has access to premium features
 *
 * Validates premium-only filter access
 */
class CheckPremiumAccessUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {

    /**
     * Check if user can access a specific filter
     *
     * @param filterType Filter to check
     * @return true if user has access, false otherwise
     */
    suspend operator fun invoke(filterType: FilterType): Boolean {
        if (!filterType.isPremium) {
            return true // Free filters are accessible to everyone
        }

        return userPreferencesRepository.isPremium()
    }

    /**
     * Check if user has premium subscription
     *
     * @return true if user is premium
     */
    suspend fun isPremium(): Boolean {
        return userPreferencesRepository.isPremium()
    }
}
