package com.photoai.editor.ui.navigation

/**
 * Navigation routes for the app
 */
sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Onboarding : Routes("onboarding")
    object Home : Routes("home")
    object Edit : Routes("edit")
    object Premium : Routes("premium")
}
