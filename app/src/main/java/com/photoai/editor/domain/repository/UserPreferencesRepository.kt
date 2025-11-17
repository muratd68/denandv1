package com.photoai.editor.domain.repository

import com.photoai.editor.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for managing user preferences and settings
 */
interface UserPreferencesRepository {

    /**
     * Get user preferences as a Flow
     * @return Flow of UserPreferences that updates when preferences change
     */
    fun getUserPreferences(): Flow<UserPreferences>

    /**
     * Update premium subscription status
     * @param isPremium New premium status
     */
    suspend fun setPremiumStatus(isPremium: Boolean)

    /**
     * Mark onboarding as completed
     */
    suspend fun setOnboardingCompleted()

    /**
     * Increment export count (used for ad frequency logic)
     */
    suspend fun incrementExportCount()

    /**
     * Check if user is premium
     * @return true if user has active premium subscription
     */
    suspend fun isPremium(): Boolean
}
