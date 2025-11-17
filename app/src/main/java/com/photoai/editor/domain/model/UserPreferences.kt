package com.photoai.editor.domain.model

/**
 * User preferences and subscription status
 * @property isPremium Whether user has active premium subscription
 * @property hasCompletedOnboarding Whether user has seen onboarding screens
 * @property exportCount Number of times user has exported images (for ad frequency)
 */
data class UserPreferences(
    val isPremium: Boolean = false,
    val hasCompletedOnboarding: Boolean = false,
    val exportCount: Int = 0
)
