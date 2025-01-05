package com.example.composepdfkit.viewer.utils

import android.content.Context
import android.net.Uri
import com.example.composepdfkit.common.utils.DocumentUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

internal class FileLoader(private val context: Context) {
    suspend fun downloadFromUrl(url: String): File = withContext(Dispatchers.IO) {
        val file = DocumentUtils.createTempFile(context, "download", "pdf")
        URL(url).openStream().use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        file
    }

    suspend fun copyFromUri(uri: Uri): File = withContext(Dispatchers.IO) {
        DocumentUtils.copyUriToFile(context, uri)
    }
}