package com.photoai.editor.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoai.editor.domain.repository.UserPreferencesRepository
import com.photoai.editor.ui.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for Splash Screen
 *
 * Determines navigation target based on onboarding status
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _navigationTarget = MutableStateFlow<String?>(null)
    val navigationTarget: StateFlow<String?> = _navigationTarget.asStateFlow()

    init {
        checkOnboardingStatus()
    }

    private fun checkOnboardingStatus() {
        viewModelScope.launch {
            // Simulate splash delay
            delay(1500)

            // Check if onboarding is completed
            userPreferencesRepository.getUserPreferences().collect { prefs ->
                _navigationTarget.value = if (prefs.hasCompletedOnboarding) {
                    Routes.Home.route
                } else {
                    Routes.Onboarding.route
                }
            }
        }
    }
}
