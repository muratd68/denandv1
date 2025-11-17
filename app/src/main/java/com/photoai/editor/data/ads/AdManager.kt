package com.photoai.editor.data.ads

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.photoai.editor.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager for AdMob advertisements
 *
 * Controls when and how ads are displayed based on user premium status
 *
 * TODO: Before publishing:
 * 1. Replace test ad unit IDs with your real ad unit IDs from AdMob
 * 2. Create ad units in AdMob console:
 *    - Banner ad unit for home screen
 *    - Interstitial ad unit for after exports
 * 3. Test ads with real devices (test ads work on emulators)
 * 4. Implement ad frequency logic (e.g., show interstitial every 3 exports)
 */
@Singleton
class AdManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository
) {

    companion object {
        // TODO: Replace with your actual AdMob ad unit IDs
        // Test IDs are used here - replace before publishing!
        const val BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111" // Test banner
        const val INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712" // Test interstitial

        // Show interstitial ad every N exports
        private const val INTERSTITIAL_FREQUENCY = 3
    }

    private var interstitialAd: InterstitialAd? = null
    private var isLoadingInterstitial = false

    /**
     * Check if ads should be shown (not premium)
     */
    suspend fun shouldShowAds(): Boolean {
        return !userPreferencesRepository.isPremium()
    }

    /**
     * Get banner ad request
     * Returns null if user is premium
     */
    suspend fun getBannerAdRequest(): AdRequest? {
        return if (shouldShowAds()) {
            AdRequest.Builder().build()
        } else {
            null
        }
    }

    /**
     * Load interstitial ad
     */
    fun loadInterstitialAd() {
        // Check if should load (not premium and not already loading)
        val shouldLoad = runBlocking {
            shouldShowAds() && !isLoadingInterstitial && interstitialAd == null
        }

        if (!shouldLoad) return

        isLoadingInterstitial = true

        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    isLoadingInterstitial = false
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    interstitialAd = null
                    isLoadingInterstitial = false
                }
            }
        )
    }

    /**
     * Show interstitial ad after export
     *
     * @param activity Activity to show ad on
     * @param onAdClosed Callback when ad is closed or not shown
     */
    suspend fun showInterstitialAfterExport(activity: Activity, onAdClosed: () -> Unit) {
        if (!shouldShowAds()) {
            onAdClosed()
            return
        }

        // Check export count for frequency
        val prefs = userPreferencesRepository.getUserPreferences().first()
        val shouldShow = prefs.exportCount % INTERSTITIAL_FREQUENCY == 0 && prefs.exportCount > 0

        if (!shouldShow) {
            onAdClosed()
            return
        }

        val ad = interstitialAd
        if (ad != null) {
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    // Ad dismissed, load next one
                    interstitialAd = null
                    loadInterstitialAd()
                    onAdClosed()
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    // Ad failed to show
                    interstitialAd = null
                    onAdClosed()
                }

                override fun onAdShowedFullScreenContent() {
                    // Ad shown, clear reference
                    interstitialAd = null
                }
            }

            ad.show(activity)
        } else {
            // Ad not loaded, load for next time
            loadInterstitialAd()
            onAdClosed()
        }
    }

    /**
     * Preload interstitial ad when app starts
     */
    fun initialize() {
        loadInterstitialAd()
    }
}
