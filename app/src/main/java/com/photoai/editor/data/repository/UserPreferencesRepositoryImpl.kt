package com.photoai.editor.data.repository

import com.photoai.editor.data.local.UserPreferencesDataSource
import com.photoai.editor.domain.model.UserPreferences
import com.photoai.editor.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of UserPreferencesRepository
 *
 * Delegates to UserPreferencesDataSource for persistence
 */
@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dataSource: UserPreferencesDataSource
) : UserPreferencesRepository {

    override fun getUserPreferences(): Flow<UserPreferences> {
        return dataSource.userPreferences
    }

    override suspend fun setPremiumStatus(isPremium: Boolean) {
        dataSource.setPremiumStatus(isPremium)
    }

    override suspend fun setOnboardingCompleted() {
        dataSource.setOnboardingCompleted()
    }

    override suspend fun incrementExportCount() {
        dataSource.incrementExportCount()
    }

    override suspend fun isPremium(): Boolean {
        return dataSource.isPremium()
    }
}
