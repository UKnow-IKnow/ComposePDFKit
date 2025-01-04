package com.example.composepdfkit.common.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.composepdfkit.models.config.CompressionQuality
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Utility class for image compression operations
 */
object CompressionUtils {

    /**
     * Compress bitmap with specified quality
     */
    fun compressBitmap(
        bitmap: Bitmap,
        quality: CompressionQuality,
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG
    ): Bitmap {
        val outputStream = ByteArrayOutputStream()

        val compressionQuality = when (quality) {
            CompressionQuality.LOW -> 30
            CompressionQuality.MEDIUM -> 60
            CompressionQuality.HIGH -> 80
            CompressionQuality.LOSSLESS -> 100
        }

        bitmap.compress(format, compressionQuality, outputStream)
        val compressedBytes = outputStream.toByteArray()
        return BitmapFactory.decodeByteArray(compressedBytes, 0, compressedBytes.size)
    }

    /**
     * Compress image file
     * @param sourceFile Source image file
     * @param quality Compression quality
     * @param maxWidth Maximum width (optional)
     * @param maxHeight Maximum height (optional)
     * @return Compressed image file
     */
    fun compressImageFile(
        sourceFile: File,
        quality: CompressionQuality,
        maxWidth: Int? = null,
        maxHeight: Int? = null
    ): File {
        // Get image dimensions
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(sourceFile.path, options)

        // Calculate scaling if needed
        val scaleFactor = if (maxWidth != null || maxHeight != null) {
            calculateScaleFactor(
                options.outWidth,
                options.outHeight,
                maxWidth ?: options.outWidth,
                maxHeight ?: options.outHeight
            )
        } else 1

        // Decode bitmap with scaling
        options.apply {
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
        }

        val bitmap = BitmapFactory.decodeFile(sourceFile.path, options)
            ?: throw IllegalArgumentException("Failed to decode image file")

        // Compress bitmap
        val compressedBitmap = compressBitmap(bitmap, quality)

        // Save to file
        val outputFile = File(
            sourceFile.parent,
            "compressed_${sourceFile.nameWithoutExtension}.jpg"
        )

        outputFile.outputStream().use { stream ->
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        }

        // Clean up
        bitmap.recycle()
        compressedBitmap.recycle()

        return outputFile
    }

    /**
     * Calculate scale factor for image resizing
     */
    private fun calculateScaleFactor(
        width: Int,
        height: Int,
        maxWidth: Int,
        maxHeight: Int
    ): Int {
        var scale = 1
        while (width / scale / 2 >= maxWidth && height / scale / 2 >= maxHeight) {
            scale *= 2
        }
        return scale
    }
}