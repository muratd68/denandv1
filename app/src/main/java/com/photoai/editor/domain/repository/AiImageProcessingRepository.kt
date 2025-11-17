package com.photoai.editor.domain.repository

import android.graphics.Bitmap
import com.photoai.editor.domain.model.FilterType

/**
 * Repository interface for AI image processing operations
 *
 * This interface abstracts the AI processing logic, allowing for easy
 * replacement with different implementations (mock, API-based, on-device model)
 */
interface AiImageProcessingRepository {

    /**
     * Apply an AI filter to an image
     *
     * @param bitmap The original image bitmap
     * @param filterType The type of filter to apply
     * @return Processed bitmap with filter applied
     * @throws Exception if processing fails
     *
     * TODO: Replace with actual AI API or on-device ML model integration
     * Current implementation is a mock/dummy for structure purposes
     */
    suspend fun applyFilter(bitmap: Bitmap, filterType: FilterType): Bitmap

    /**
     * Check if AI service is available
     * @return true if service is ready, false otherwise
     */
    suspend fun isServiceAvailable(): Boolean
}
