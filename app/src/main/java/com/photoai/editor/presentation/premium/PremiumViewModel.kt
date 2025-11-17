package com.photoai.editor.presentation.premium

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoai.editor.data.billing.BillingManager
import com.photoai.editor.data.billing.BillingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PremiumViewModel @Inject constructor(
    private val billingManager: BillingManager
) : ViewModel() {

    val billingState: StateFlow<BillingState> = billingManager.billingState

    init {
        billingManager.initialize()
    }

    fun purchasePremium(activity: Activity) {
        billingManager.launchPremiumPurchase(activity)
    }

    fun restorePurchase() {
        billingManager.queryPurchases()
    }

    override fun onCleared() {
        super.onCleared()
        billingManager.destroy()
    }
}
