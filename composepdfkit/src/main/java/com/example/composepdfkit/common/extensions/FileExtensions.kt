package com.example.composepdfkit.common.extensions

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * Extension functions for File operations
 */

/**
 * Copy file to cache directory
 * @param context Application context
 * @param fileName Target file name
 * @return Cached file
 */
fun File.copyToCache(context: Context, fileName: String): File {
    val cacheFile = File(context.cacheDir, fileName)
    this.inputStream().use { input ->
        FileOutputStream(cacheFile).use { output ->
            input.copyTo(output)
        }
    }
    return cacheFile
}

/**
 * Convert Uri to File
 * @param context Application context
 * @param fileName Target file name
 * @throws IOException if URI cannot be read
 */
fun Uri.toFile(context: Context, fileName: String): File {
    val destinationFile = File(context.cacheDir, fileName)
    context.contentResolver.openInputStream(this)?.use { input ->
        FileOutputStream(destinationFile).use { output ->
            input.copyTo(output)
        }
    } ?: throw IOException("Failed to read URI: $this")
    return destinationFile
}

/**
 * Create file and parent directories if they don't exist
 */
fun File.ensureFileExists(): File {
    if (!exists()) {
        parentFile?.mkdirs()
        createNewFile()
    }
    return this
}

/**
 * Get file extension safely
 */
val File.extensionOrEmpty: String
    get() = extension.takeIf { it.isNotEmpty() } ?: ""

/**
 * Create temporary file in cache directory
 */
fun Context.createTempFile(prefix: String, extension: String): File {
    return File.createTempFile(
        "${prefix}_${System.currentTimeMillis()}",
        ".$extension",
        cacheDir
    )
}
