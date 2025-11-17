package com.photoai.editor.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.photoai.editor.presentation.splash.SplashViewModel
import com.photoai.editor.ui.screens.edit.EditScreen
import com.photoai.editor.ui.screens.home.HomeScreen
import com.photoai.editor.ui.screens.onboarding.OnboardingScreen
import com.photoai.editor.ui.screens.premium.PremiumScreen
import com.photoai.editor.ui.screens.splash.SplashScreen

/**
 * Main navigation component for the app
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        // Splash Screen
        composable(Routes.Splash.route) {
            val viewModel: SplashViewModel = hiltViewModel()
            val navigationTarget by viewModel.navigationTarget.collectAsState()

            SplashScreen(
                navigationTarget = navigationTarget,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Onboarding Screen
        composable(Routes.Onboarding.route) {
            OnboardingScreen(
                onFinish = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        // Home Screen
        composable(Routes.Home.route) {
            HomeScreen(
                onImageSelected = {
                    navController.navigate(Routes.Edit.route)
                },
                onPremiumClick = {
                    navController.navigate(Routes.Premium.route)
                }
            )
        }

        // Edit Screen
        composable(Routes.Edit.route) {
            EditScreen(
                onBack = {
                    navController.popBackStack()
                },
                onPremiumClick = {
                    navController.navigate(Routes.Premium.route)
                }
            )
        }

        // Premium Screen
        composable(Routes.Premium.route) {
            PremiumScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
