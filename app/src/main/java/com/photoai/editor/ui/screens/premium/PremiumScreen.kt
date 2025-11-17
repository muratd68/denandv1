package com.photoai.editor.ui.screens.premium

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.photoai.editor.R
import com.photoai.editor.data.billing.BillingState
import com.photoai.editor.presentation.premium.PremiumViewModel
import com.photoai.editor.ui.components.LoadingIndicator
import com.photoai.editor.ui.components.PrimaryButton
import com.photoai.editor.ui.theme.GoldPremium
import com.photoai.editor.ui.theme.PrimaryBlue
import com.photoai.editor.ui.theme.PrimaryPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumScreen(
    onBack: () -> Unit,
    viewModel: PremiumViewModel = hiltViewModel()
) {
    val context = LocalContext.current as Activity
    val billingState by viewModel.billingState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(billingState) {
        when (val state = billingState) {
            is BillingState.Error -> {
                snackbarHostState.showSnackbar(state.message)
            }
            is BillingState.PurchaseSuccess -> {
                snackbarHostState.showSnackbar("Premium activated! 🎉")
            }
            is BillingState.Canceled -> {
                snackbarHostState.showSnackbar("Purchase canceled")
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.premium_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Premium header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(PrimaryPurple, PrimaryBlue)
                            ),
                            shape = MaterialTheme.shapes.large
                        )
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "⭐ PREMIUM",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.premium_subtitle),
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                // Features list
                PremiumFeature(stringResource(R.string.premium_feature_1))
                PremiumFeature(stringResource(R.string.premium_feature_2))
                PremiumFeature(stringResource(R.string.premium_feature_3))
                PremiumFeature(stringResource(R.string.premium_feature_4))

                Spacer(Modifier.height(32.dp))

                // Pricing
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = GoldPremium.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.premium_price),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = GoldPremium
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = "Cancel anytime",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                // Purchase button
                PrimaryButton(
                    text = stringResource(R.string.upgrade_now),
                    onClick = { viewModel.purchasePremium(context) },
                    enabled = billingState !is BillingState.Loading,
                    backgroundColor = GoldPremium
                )

                Spacer(Modifier.height(16.dp))

                // Restore button
                TextButton(
                    onClick = { viewModel.restorePurchase() }
                ) {
                    Text(stringResource(R.string.restore_purchase))
                }

                Spacer(Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.terms_and_privacy),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center
                )
            }

            // Loading overlay
            if (billingState is BillingState.Loading) {
                LoadingIndicator("Processing purchase...")
            }
        }
    }
}

@Composable
private fun PremiumFeature(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
