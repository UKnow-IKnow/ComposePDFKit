package com.example.composepdfkit.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.os.ParcelFileDescriptor
import android.graphics.pdf.PdfRenderer
import com.example.composepdfkit.models.config.PageOrientation
import java.io.File

/**
 * Utility class for document operations
 */
object DocumentUtils {

    /**
     * Create empty PDF document
     */
    fun createEmptyDocument(): PdfDocument = PdfDocument()

    /**
     * Get total page count from document file
     * @throws IOException if file cannot be read
     */
    fun getPageCount(file: File): Int {
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).use { fd ->
            PdfRenderer(fd).use { renderer ->
                renderer.pageCount
            }
        }
    }

    /**
     * Extract range of pages from document
     * @param context Application context
     * @param sourceFile Source document file
     * @param startPage Starting page index (0-based)
     * @param endPage Ending page index (inclusive)
     * @return File containing extracted pages
     */
    fun extractPageRange(
        context: Context,
        sourceFile: File,
        startPage: Int,
        endPage: Int
    ): File {
        require(startPage >= 0) { "Start page must be non-negative" }
        require(endPage >= startPage) { "End page must be greater than or equal to start page" }

        val outputFile = File(context.cacheDir, "extracted_${System.currentTimeMillis()}.pdf")
        val document = PdfDocument()

        ParcelFileDescriptor.open(sourceFile, ParcelFileDescriptor.MODE_READ_ONLY).use { fd ->
            PdfRenderer(fd).use { renderer ->
                require(startPage < renderer.pageCount) { "Start page out of range" }

                for (pageIndex in startPage..minOf(endPage, renderer.pageCount - 1)) {
                    renderer.openPage(pageIndex).use { page ->
                        val pageInfo = PdfDocument.PageInfo.Builder(
                            page.width,
                            page.height,
                            pageIndex - startPage
                        ).create()

                        // Create a bitmap with the page dimensions
                        val bitmap = Bitmap.createBitmap(
                            page.width,
                            page.height,
                            Bitmap.Config.ARGB_8888
                        )

                        // Render the PDF page into the bitmap
                        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                        // Create the PDF page and draw the bitmap onto its canvas
                        document.startPage(pageInfo).apply {
                            canvas.drawBitmap(bitmap, 0f, 0f, null)
                            document.finishPage(this)
                        }

                        // Recycle the bitmap to free memory
                        bitmap.recycle()
                    }
                }
            }
        }

        outputFile.outputStream().use { stream ->
            document.writeTo(stream)
        }
        document.close()

        return outputFile
    }

    /**
     * Merge multiple document files into one
     * @param context Application context
     * @param files List of files to merge
     * @return Merged document file
     */
    fun mergeFiles(context: Context, files: List<File>): File {
        require(files.isNotEmpty()) { "File list cannot be empty" }

        val outputFile = File(context.cacheDir, "merged_${System.currentTimeMillis()}.pdf")
        val mergedDoc = PdfDocument()
        var currentPage = 0

        files.forEach { file ->
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).use { fd ->
                PdfRenderer(fd).use { renderer ->
                    for (pageIndex in 0 until renderer.pageCount) {
                        renderer.openPage(pageIndex).use { page ->
                            val pageInfo = PdfDocument.PageInfo.Builder(
                                page.width,
                                page.height,
                                currentPage++
                            ).create()

                            // Create a bitmap with the page dimensions
                            val bitmap = Bitmap.createBitmap(
                                page.width,
                                page.height,
                                Bitmap.Config.ARGB_8888
                            )

                            // Render the PDF page into the bitmap
                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)

                            // Create the PDF page and draw the bitmap onto its canvas
                            mergedDoc.startPage(pageInfo).apply {
                                canvas.drawBitmap(bitmap, 0f, 0f, null)
                                mergedDoc.finishPage(this)
                            }

                            // Recycle the bitmap to free memory
                            bitmap.recycle()
                        }
                    }
                }
            }
        }

        outputFile.outputStream().use { stream ->
            mergedDoc.writeTo(stream)
        }
        mergedDoc.close()

        return outputFile
    }
}
