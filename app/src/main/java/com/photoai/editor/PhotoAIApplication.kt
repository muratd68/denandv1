package com.photoai.editor

import android.app.Application
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for PhotoAI Editor
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection
 */
@HiltAndroidApp
class PhotoAIApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Mobile Ads SDK
        MobileAds.initialize(this) { initializationStatus ->
            // Log initialization status if needed
            // In production, you might want to log this for debugging
        }
    }
}
