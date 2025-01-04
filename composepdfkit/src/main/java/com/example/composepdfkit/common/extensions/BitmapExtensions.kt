package com.example.composepdfkit.common.extensions

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.Canvas
import android.graphics.Color
import java.io.ByteArrayOutputStream

/**
 * Extension functions for Bitmap operations
 */

/**
 * Resize bitmap maintaining aspect ratio
 * @param maxWidth Maximum width of the resulting bitmap
 * @param maxHeight Maximum height of the resulting bitmap
 * @return Resized bitmap
 */
fun Bitmap.resize(maxWidth: Int, maxHeight: Int): Bitmap {
    if (width <= maxWidth && height <= maxHeight) return this

    val ratioX = maxWidth.toFloat() / width
    val ratioY = maxHeight.toFloat() / height
    val ratio = minOf(ratioX, ratioY)

    val newWidth = (width * ratio).toInt()
    val newHeight = (height * ratio).toInt()

    return Bitmap.createScaledBitmap(this, newWidth, newHeight, true)
}

/**
 * Rotate bitmap by specified degrees
 * @param degrees Rotation angle in degrees
 * @return Rotated bitmap
 */
fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(degrees) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

/**
 * Convert bitmap to byte array with specified format and quality
 */
fun Bitmap.toByteArray(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    quality: Int = 100
): ByteArray {
    return ByteArrayOutputStream().use { stream ->
        compress(format, quality, stream)
        stream.toByteArray()
    }
}

/**
 * Create a copy of bitmap with white background
 */
fun Bitmap.withWhiteBackground(): Bitmap {
    if (!hasAlpha()) return this

    val newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(newBitmap)
    canvas.drawColor(Color.WHITE)
    canvas.drawBitmap(this, 0f, 0f, null)
    return newBitmap
}