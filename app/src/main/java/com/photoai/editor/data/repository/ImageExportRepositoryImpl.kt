package com.photoai.editor.data.repository

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.photoai.editor.domain.repository.ImageExportRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of ImageExportRepository
 *
 * Handles saving images to gallery and adding watermarks
 */
@Singleton
class ImageExportRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ImageExportRepository {

    override suspend fun saveImageToGallery(bitmap: Bitmap, addWatermark: Boolean): Uri? =
        withContext(Dispatchers.IO) {
            try {
                val imageToSave = if (addWatermark) {
                    addWatermark(bitmap)
                } else {
                    bitmap
                }

                val filename = "PhotoAI_${System.currentTimeMillis()}.jpg"

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // For Android 10+, use MediaStore
                    val contentValues = ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/PhotoAI")
                    }

                    val uri = context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )

                    uri?.let {
                        context.contentResolver.openOutputStream(it)?.use { outputStream ->
                            imageToSave.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
                        }
                    }

                    uri
                } else {
                    // For Android 9 and below
                    val picturesDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                    )
                    val photoAIDir = File(picturesDir, "PhotoAI")
                    if (!photoAIDir.exists()) {
                        photoAIDir.mkdirs()
                    }

                    val file = File(photoAIDir, filename)
                    FileOutputStream(file).use { outputStream ->
                        imageToSave.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
                    }

                    // Notify media scanner
                    MediaStore.Images.Media.insertImage(
                        context.contentResolver,
                        file.absolutePath,
                        filename,
                        "Edited with PhotoAI"
                    )

                    Uri.fromFile(file)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    override suspend fun addWatermark(bitmap: Bitmap): Bitmap = withContext(Dispatchers.IO) {
        val result = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(result)

        // Draw original image
        canvas.drawBitmap(bitmap, 0f, 0f, null)

        // Configure watermark text
        val watermarkText = "PhotoAI"
        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = bitmap.width * 0.04f // 4% of image width
            isAntiAlias = true
            alpha = 180 // Semi-transparent
            setShadowLayer(2f, 2f, 2f, Color.BLACK) // Text shadow for visibility
        }

        // Calculate position (bottom-right corner with padding)
        val padding = bitmap.width * 0.02f
        val textWidth = textPaint.measureText(watermarkText)
        val x = bitmap.width - textWidth - padding
        val y = bitmap.height - padding

        // Draw watermark
        canvas.drawText(watermarkText, x, y, textPaint)

        result
    }

    override suspend fun getShareableUri(bitmap: Bitmap, addWatermark: Boolean): Uri? =
        withContext(Dispatchers.IO) {
            try {
                val imageToShare = if (addWatermark) {
                    addWatermark(bitmap)
                } else {
                    bitmap
                }

                val cachePath = File(context.cacheDir, "images")
                cachePath.mkdirs()

                val file = File(cachePath, "share_${System.currentTimeMillis()}.jpg")
                FileOutputStream(file).use { outputStream ->
                    imageToShare.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
                }

                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
}
