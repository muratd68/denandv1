package com.photoai.editor.domain.usecase

import android.graphics.Bitmap
import android.net.Uri
import com.photoai.editor.domain.repository.ImageExportRepository
import com.photoai.editor.domain.repository.UserPreferencesRepository
import javax.inject.Inject

/**
 * Use case for exporting images to gallery
 *
 * Handles watermark logic based on premium status
 */
class ExportImageUseCase @Inject constructor(
    private val imageExportRepository: ImageExportRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) {

    /**
     * Export image to gallery
     *
     * Automatically adds watermark for free users
     *
     * @param bitmap Image to export
     * @return Result containing Uri of saved image or error
     */
    suspend operator fun invoke(bitmap: Bitmap): Result<Uri> {
        return try {
            val isPremium = userPreferencesRepository.isPremium()
            val shouldAddWatermark = !isPremium

            val uri = imageExportRepository.saveImageToGallery(bitmap, shouldAddWatermark)
                ?: return Result.failure(Exception("Failed to save image"))

            // Increment export count for ad frequency logic
            userPreferencesRepository.incrementExportCount()

            Result.success(uri)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
