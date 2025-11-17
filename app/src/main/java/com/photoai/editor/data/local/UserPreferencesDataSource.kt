package com.photoai.editor.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.photoai.editor.domain.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataStore-based data source for user preferences
 *
 * Handles persistence of user settings and premium status
 */
@Singleton
class UserPreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        val KEY_IS_PREMIUM = booleanPreferencesKey("is_premium")
        val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val KEY_EXPORT_COUNT = intPreferencesKey("export_count")
    }

    /**
     * Get user preferences as Flow
     */
    val userPreferences: Flow<UserPreferences> = dataStore.data.map { preferences ->
        UserPreferences(
            isPremium = preferences[KEY_IS_PREMIUM] ?: false,
            hasCompletedOnboarding = preferences[KEY_ONBOARDING_COMPLETED] ?: false,
            exportCount = preferences[KEY_EXPORT_COUNT] ?: 0
        )
    }

    /**
     * Update premium status
     */
    suspend fun setPremiumStatus(isPremium: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_IS_PREMIUM] = isPremium
        }
    }

    /**
     * Mark onboarding as completed
     */
    suspend fun setOnboardingCompleted() {
        dataStore.edit { preferences ->
            preferences[KEY_ONBOARDING_COMPLETED] = true
        }
    }

    /**
     * Increment export count
     */
    suspend fun incrementExportCount() {
        dataStore.edit { preferences ->
            val currentCount = preferences[KEY_EXPORT_COUNT] ?: 0
            preferences[KEY_EXPORT_COUNT] = currentCount + 1
        }
    }

    /**
     * Check if user is premium
     */
    suspend fun isPremium(): Boolean {
        return dataStore.data.first()[KEY_IS_PREMIUM] ?: false
    }
}
