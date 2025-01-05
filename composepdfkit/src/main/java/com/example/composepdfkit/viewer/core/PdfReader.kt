package com.example.composepdfkit.viewer.core

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import com.example.composepdfkit.viewer.model.Document
import com.example.composepdfkit.viewer.model.DocumentPage
import com.example.composepdfkit.viewer.model.DocumentSource
import com.example.composepdfkit.viewer.utils.FileLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

internal class PdfReader(
    context: Context
) : DocumentReader {
    private val fileLoader = FileLoader(context)

    override suspend fun readFromSource(
        source: DocumentSource,
        isImage: Boolean
    ): Document = withContext(Dispatchers.IO) {
        val file = when (source) {
            is DocumentSource.Url -> fileLoader.downloadFromUrl(source.url)
            is DocumentSource.Uri -> fileLoader.copyFromUri(source.uri)
        }

        if (isImage) {
            readImageDocument(file)
        } else {
            readPdfDocument(file)
        }
    }

    private fun readPdfDocument(file: File): Document {
        val renderer = PdfRenderer(
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        )

        return Document(
            pageCount = renderer.pageCount,
            getPage = { pageIndex ->
                renderer.openPage(pageIndex).use { page ->
                    DocumentPage(
                        width = page.width,
                        height = page.height,
                        render = { scale ->
                            val bitmap = Bitmap.createBitmap(
                                (page.width * scale).toInt(),
                                (page.height * scale).toInt(),
                                Bitmap.Config.ARGB_8888
                            )
                            page.render(
                                bitmap,
                                null,
                                null,
                                PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY
                            )
                            bitmap
                        }
                    )
                }
            },
            close = {
                renderer.close()
                file.delete()
            }
        )
    }

    private fun readImageDocument(file: File): Document {
        val bitmap = BitmapFactory.decodeFile(file.path)
            ?: throw IllegalArgumentException("Failed to decode image")

        return Document(
            pageCount = 1,
            getPage = { _ ->
                DocumentPage(
                    width = bitmap.width,
                    height = bitmap.height,
                    render = { scale ->
                        if (scale == 1f) {
                            bitmap
                        } else {
                            Bitmap.createScaledBitmap(
                                bitmap,
                                (bitmap.width * scale).toInt(),
                                (bitmap.height * scale).toInt(),
                                true
                            )
                        }
                    }
                )
            },
            close = {
                bitmap.recycle()
                file.delete()
            }
        )
    }
}