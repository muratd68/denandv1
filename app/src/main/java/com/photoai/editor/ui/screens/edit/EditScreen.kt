package com.photoai.editor.ui.screens.edit

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.photoai.editor.R
import com.photoai.editor.domain.model.FilterType
import com.photoai.editor.presentation.edit.EditUiState
import com.photoai.editor.presentation.edit.EditViewModel
import com.photoai.editor.ui.components.FilterChip
import com.photoai.editor.ui.components.LoadingIndicator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    onBack: () -> Unit,
    onPremiumClick: () -> Unit,
    viewModel: EditViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val originalBitmap by viewModel.originalBitmap.collectAsState()
    val editedBitmap by viewModel.editedBitmap.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val showOriginal by viewModel.showOriginal.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is EditUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            is EditUiState.ExportSuccess -> {
                snackbarHostState.showSnackbar(context.getString(R.string.image_saved))
                viewModel.resetState()
            }
            is EditUiState.PremiumRequired -> {
                snackbarHostState.showSnackbar(context.getString(R.string.premium_feature_locked))
                viewModel.resetState()
                onPremiumClick()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.edit_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.exportImage() }) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                    IconButton(onClick = {
                        // Share functionality
                        val bitmap = editedBitmap ?: selectedImage
                        bitmap?.let {
                            // TODO: Implement share with getShareableUri
                        }
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
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
                modifier = Modifier.fillMaxSize()
            ) {
                // Image display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onPress = {
                                    viewModel.toggleShowOriginal()
                                    tryAwaitRelease()
                                    viewModel.toggleShowOriginal()
                                }
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val displayBitmap = when {
                        showOriginal -> originalBitmap
                        editedBitmap != null -> editedBitmap
                        else -> originalBitmap
                    }

                    displayBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Photo",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                // Filter selection
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Filters",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(FilterType.values().toList()) { filter ->
                            FilterChip(
                                text = filter.displayName,
                                isSelected = selectedFilter == filter,
                                isPremium = filter.isPremium,
                                onClick = { viewModel.applyFilter(filter) }
                            )
                        }
                    }
                }
            }

            // Loading overlay
            if (uiState is EditUiState.Loading) {
                LoadingIndicator(stringResource(R.string.processing))
            }

            if (uiState is EditUiState.Exporting) {
                LoadingIndicator("Saving...")
            }
        }
    }
}
