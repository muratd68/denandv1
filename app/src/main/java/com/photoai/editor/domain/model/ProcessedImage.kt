package com.photoai.editor.domain.model

import android.graphics.Bitmap

/**
 * Represents a processed image with original and edited versions
 * @property originalBitmap The original image bitmap
 * @property editedBitmap The edited image bitmap (nullable, null if not yet processed)
 * @property appliedFilter The filter that was applied
 */
data class ProcessedImage(
    val originalBitmap: Bitmap,
    val editedBitmap: Bitmap? = null,
    val appliedFilter: FilterType? = null
)
