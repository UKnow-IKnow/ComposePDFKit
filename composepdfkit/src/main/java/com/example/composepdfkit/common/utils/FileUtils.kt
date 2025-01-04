package com.example.composepdfkit.common.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.InputStream

internal object FileUtils {
    fun createTempFile(context: Context, prefix: String, extension: String): File {
        return File(context.cacheDir, "${prefix}_${System.currentTimeMillis()}.$extension")
    }

    fun copyUriToFile(context: Context, uri: Uri): File {
        val tempFile = createTempFile(context, "temp", "pdf")
        context.contentResolver.openInputStream(uri)?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }
}