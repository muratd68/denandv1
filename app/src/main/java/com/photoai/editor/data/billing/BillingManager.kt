package com.photoai.editor.data.billing

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.*
import com.photoai.editor.domain.repository.UserPreferencesRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager for Google Play Billing operations
 *
 * Handles subscription purchases and restoration
 *
 * TODO: Before publishing:
 * 1. Create subscription product in Google Play Console
 * 2. Replace PREMIUM_MONTHLY_PRODUCT_ID with your actual product ID
 * 3. Test with real Google Play accounts (not test accounts)
 * 4. Implement proper error handling and logging
 * 5. Add security: verify purchases on your backend server
 */
@Singleton
class BillingManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository
) : PurchasesUpdatedListener {

    companion object {
        // TODO: Replace with your actual subscription product ID from Play Console
        private const val PREMIUM_MONTHLY_PRODUCT_ID = "premium_monthly"
    }

    private val _billingState = MutableStateFlow<BillingState>(BillingState.Idle)
    val billingState: StateFlow<BillingState> = _billingState.asStateFlow()

    private var billingClient: BillingClient? = null
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    /**
     * Initialize billing client
     */
    fun initialize() {
        billingClient = BillingClient.newBuilder(context)
            .setListener(this)
            .enablePendingPurchases()
            .build()

        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    _billingState.value = BillingState.Ready
                    // Check for existing purchases
                    queryPurchases()
                } else {
                    _billingState.value = BillingState.Error("Billing setup failed: ${billingResult.debugMessage}")
                }
            }

            override fun onBillingServiceDisconnected() {
                _billingState.value = BillingState.Disconnected
                // Retry connection
            }
        })
    }

    /**
     * Launch purchase flow for premium subscription
     */
    fun launchPremiumPurchase(activity: Activity) {
        val client = billingClient ?: run {
            _billingState.value = BillingState.Error("Billing not initialized")
            return
        }

        _billingState.value = BillingState.Loading

        val queryProductDetailsParams = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PREMIUM_MONTHLY_PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            )
            .build()

        client.queryProductDetailsAsync(queryProductDetailsParams) { billingResult, productDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && productDetailsList.isNotEmpty()) {
                val productDetails = productDetailsList[0]
                val offerToken = productDetails.subscriptionOfferDetails?.get(0)?.offerToken

                if (offerToken != null) {
                    val productDetailsParamsList = listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(productDetails)
                            .setOfferToken(offerToken)
                            .build()
                    )

                    val billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build()

                    client.launchBillingFlow(activity, billingFlowParams)
                } else {
                    _billingState.value = BillingState.Error("No subscription offers available")
                }
            } else {
                _billingState.value = BillingState.Error("Failed to query products: ${billingResult.debugMessage}")
            }
        }
    }

    /**
     * Query existing purchases
     */
    fun queryPurchases() {
        val client = billingClient ?: return

        client.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder()
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        ) { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                handlePurchases(purchases)
            }
        }
    }

    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.let { handlePurchases(it) }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                _billingState.value = BillingState.Canceled
            }
            else -> {
                _billingState.value = BillingState.Error("Purchase failed: ${billingResult.debugMessage}")
            }
        }
    }

    private fun handlePurchases(purchases: List<Purchase>) {
        var hasPremium = false

        for (purchase in purchases) {
            if (purchase.products.contains(PREMIUM_MONTHLY_PRODUCT_ID) &&
                purchase.purchaseState == Purchase.PurchaseState.PURCHASED
            ) {
                hasPremium = true

                // Acknowledge purchase if not already acknowledged
                if (!purchase.isAcknowledged) {
                    acknowledgePurchase(purchase)
                }
            }
        }

        // Update premium status
        coroutineScope.launch {
            userPreferencesRepository.setPremiumStatus(hasPremium)
            _billingState.value = if (hasPremium) {
                BillingState.PurchaseSuccess
            } else {
                BillingState.Ready
            }
        }
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val client = billingClient ?: return

        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        client.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                // Log error
            }
        }
    }

    fun destroy() {
        billingClient?.endConnection()
        billingClient = null
    }
}

/**
 * Billing state representation
 */
sealed class BillingState {
    object Idle : BillingState()
    object Loading : BillingState()
    object Ready : BillingState()
    object Disconnected : BillingState()
    object PurchaseSuccess : BillingState()
    object Canceled : BillingState()
    data class Error(val message: String) : BillingState()
}
