package com.photoai.editor.domain.repository

import android.graphics.Bitmap
import android.net.Uri

/**
 * Repository interface for image export and sharing operations
 */
interface ImageExportRepository {

    /**
     * Save image to device gallery
     *
     * @param bitmap Image to save
     * @param addWatermark Whether to add watermark (based on premium status)
     * @return Uri of saved image, or null if failed
     */
    suspend fun saveImageToGallery(bitmap: Bitmap, addWatermark: Boolean): Uri?

    /**
     * Add watermark to image
     *
     * @param bitmap Original image
     * @return Bitmap with watermark applied
     */
    suspend fun addWatermark(bitmap: Bitmap): Bitmap

    /**
     * Get shareable URI for an image
     *
     * @param bitmap Image to share
     * @param addWatermark Whether to add watermark
     * @return Uri that can be used with share intent
     */
    suspend fun getShareableUri(bitmap: Bitmap, addWatermark: Boolean): Uri?
}
