package com.photoai.editor.domain.usecase

import android.graphics.Bitmap
import com.photoai.editor.domain.model.FilterType
import com.photoai.editor.domain.repository.AiImageProcessingRepository
import javax.inject.Inject

/**
 * Use case for applying AI filters to images
 *
 * Encapsulates the business logic for filter application
 */
class ApplyFilterUseCase @Inject constructor(
    private val aiRepository: AiImageProcessingRepository
) {

    /**
     * Apply filter to image
     *
     * @param bitmap Original image
     * @param filterType Filter to apply
     * @return Result containing processed bitmap or error
     */
    suspend operator fun invoke(
        bitmap: Bitmap,
        filterType: FilterType
    ): Result<Bitmap> {
        return try {
            val processedBitmap = aiRepository.applyFilter(bitmap, filterType)
            Result.success(processedBitmap)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
