package com.photoai.editor.presentation.home

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.photoai.editor.data.ads.AdManager
import com.photoai.editor.domain.repository.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val adManager: AdManager
) : ViewModel() {

    private val _selectedImage = MutableStateFlow<Bitmap?>(null)
    val selectedImage: StateFlow<Bitmap?> = _selectedImage.asStateFlow()

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium.asStateFlow()

    init {
        viewModelScope.launch {
            userPreferencesRepository.getUserPreferences().collect { prefs ->
                _isPremium.value = prefs.isPremium
            }
        }
        adManager.initialize()
    }

    fun setSelectedImage(bitmap: Bitmap) {
        _selectedImage.value = bitmap
    }
}
