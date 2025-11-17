package com.photoai.editor.data.local

import android.graphics.Bitmap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * In-memory cache for the currently selected image
 *
 * This singleton holds the selected image to share between ViewModels
 * without passing large bitmaps through navigation arguments
 *
 * IMPORTANT: Remember to clear() when done to prevent memory leaks
 */
@Singleton
class ImageCache @Inject constructor() {

    private var currentImage: Bitmap? = null

    /**
     * Store the selected image
     */
    fun setImage(bitmap: Bitmap) {
        // Recycle old bitmap if exists
        currentImage?.let {
            if (!it.isRecycled) {
                // Don't recycle here as it might be in use
                // Let GC handle it
            }
        }
        currentImage = bitmap
    }

    /**
     * Get the currently cached image
     */
    fun getImage(): Bitmap? = currentImage

    /**
     * Clear the cache
     * Call this when leaving edit screen or app is destroyed
     */
    fun clear() {
        currentImage = null
    }
}
