package com.photoai.editor.presentation.edit

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoai.editor.data.local.ImageCache
import com.photoai.editor.domain.model.FilterType
import com.photoai.editor.domain.usecase.ApplyFilterUseCase
import com.photoai.editor.domain.usecase.CheckPremiumAccessUseCase
import com.photoai.editor.domain.usecase.ExportImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val applyFilterUseCase: ApplyFilterUseCase,
    private val exportImageUseCase: ExportImageUseCase,
    private val checkPremiumAccessUseCase: CheckPremiumAccessUseCase,
    private val imageCache: ImageCache
) : ViewModel() {

    private val _uiState = MutableStateFlow<EditUiState>(EditUiState.Idle)
    val uiState: StateFlow<EditUiState> = _uiState.asStateFlow()

    private val _originalBitmap = MutableStateFlow<Bitmap?>(null)
    val originalBitmap: StateFlow<Bitmap?> = _originalBitmap.asStateFlow()

    private val _editedBitmap = MutableStateFlow<Bitmap?>(null)
    val editedBitmap: StateFlow<Bitmap?> = _editedBitmap.asStateFlow()

    private val _selectedFilter = MutableStateFlow<FilterType?>(null)
    val selectedFilter: StateFlow<FilterType?> = _selectedFilter.asStateFlow()

    private val _showOriginal = MutableStateFlow(false)
    val showOriginal: StateFlow<Boolean> = _showOriginal.asStateFlow()

    init {
        // Load image from cache when ViewModel is created
        _originalBitmap.value = imageCache.getImage()
    }

    fun setOriginalImage(bitmap: Bitmap) {
        _originalBitmap.value = bitmap
    }

    fun applyFilter(filterType: FilterType) {
        viewModelScope.launch {
            val hasAccess = checkPremiumAccessUseCase(filterType)
            if (!hasAccess) {
                _uiState.value = EditUiState.PremiumRequired
                return@launch
            }

            _uiState.value = EditUiState.Loading
            val bitmap = _originalBitmap.value ?: return@launch

            val result = applyFilterUseCase(bitmap, filterType)
            result.fold(
                onSuccess = { processedBitmap ->
                    _editedBitmap.value = processedBitmap
                    _selectedFilter.value = filterType
                    _uiState.value = EditUiState.Success
                },
                onFailure = { error ->
                    _uiState.value = EditUiState.Error(error.message ?: "Unknown error")
                }
            )
        }
    }

    fun exportImage() {
        viewModelScope.launch {
            _uiState.value = EditUiState.Exporting
            val bitmap = _editedBitmap.value ?: _originalBitmap.value ?: return@launch

            val result = exportImageUseCase(bitmap)
            result.fold(
                onSuccess = {
                    _uiState.value = EditUiState.ExportSuccess
                },
                onFailure = { error ->
                    _uiState.value = EditUiState.Error(error.message ?: "Export failed")
                }
            )
        }
    }

    fun toggleShowOriginal() {
        _showOriginal.value = !_showOriginal.value
    }

    fun resetState() {
        _uiState.value = EditUiState.Idle
    }
}

sealed class EditUiState {
    object Idle : EditUiState()
    object Loading : EditUiState()
    object Success : EditUiState()
    object Exporting : EditUiState()
    object ExportSuccess : EditUiState()
    object PremiumRequired : EditUiState()
    data class Error(val message: String) : EditUiState()
}
