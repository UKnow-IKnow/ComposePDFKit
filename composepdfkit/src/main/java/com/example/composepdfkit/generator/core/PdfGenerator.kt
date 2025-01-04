package com.example.composepdfkit.generator.core

import android.content.Context
import android.graphics.pdf.PdfDocument
import androidx.compose.runtime.Composable
import com.example.composepdfkit.common.config.PageConfig
import com.example.composepdfkit.common.config.PageOrientation
import com.example.composepdfkit.common.exception.DocumentKitException
import com.example.composepdfkit.generator.config.GeneratorConfig
import com.example.composepdfkit.generator.model.DocumentConfig
import com.example.composepdfkit.generator.utils.ComposeViewUtils
import java.io.File
import java.io.FileOutputStream

internal class PdfGenerator(
    private val context: Context
) : DocumentGenerator {
    private val viewUtils = ComposeViewUtils(context)

    override fun generate(config: GeneratorConfig): File = runCatching {
        val documentConfig = createDocumentConfig(config)
        val pdfDocument = createDocument(documentConfig)
        return saveDocument(pdfDocument, documentConfig)
    }.getOrElse { error ->
        throw DocumentKitException.GenerationError(
            "Failed to generate document: ${error.message}",
            error
        )
    }

    private fun createDocumentConfig(config: GeneratorConfig): DocumentConfig {
        val headerView = config.header?.let { header: @Composable () -> Unit ->
            viewUtils.createComposeView(header)
        }
        val footerView = config.footer?.let { footer: @Composable () -> Unit ->
            viewUtils.createComposeView(footer)
        }
        val bodyView = viewUtils.createComposeView(config.body)

        return DocumentConfig(
            name = config.name,
            pageConfig = config.pageConfig,
            metadata = config.metadata,
            headerView = headerView,
            footerView = footerView,
            bodyView = bodyView
        )
    }

    private fun createDocument(documentConfig: DocumentConfig): PdfDocument {
        val measurements = viewUtils.measureViews(
            documentConfig.headerView,
            documentConfig.bodyView,
            documentConfig.footerView
        )

        val pdfDocument = PdfDocument()
        val (width, height) = getPageDimensions(documentConfig.pageConfig)

        generatePages(
            document = pdfDocument,
            width = width,
            height = height,
            measurements = measurements
        )

        return pdfDocument
    }

    private fun generatePages(
        document: PdfDocument,
        width: Int,
        height: Int,
        measurements: ComposeViewUtils.ViewMeasurements
    ) {
        val availableHeight = calculateAvailableHeight(height, measurements)
        val totalPages = calculateTotalPages(measurements.bodyHeight, availableHeight)

        for (pageNumber in 1..totalPages) {
            val page = createPage(document, width, height, pageNumber)
            drawPageContent(
                page = page,
                pageNumber = pageNumber,
                measurements = measurements,
                availableHeight = availableHeight
            )
            document.finishPage(page)
        }
    }

    private fun calculateAvailableHeight(
        height: Int,
        measurements: ComposeViewUtils.ViewMeasurements
    ): Int {
        return height -
                (measurements.headerHeight ?: 0) -
                (measurements.footerHeight ?: 0)
    }

    private fun calculateTotalPages(bodyHeight: Int, availableHeight: Int): Int {
        return (bodyHeight / availableHeight) +
                if (bodyHeight % availableHeight > 0) 1 else 0
    }

    private fun createPage(
        document: PdfDocument,
        width: Int,
        height: Int,
        pageNumber: Int
    ): PdfDocument.Page {
        val pageInfo = PdfDocument.PageInfo.Builder(width, height, pageNumber).create()
        return document.startPage(pageInfo)
    }

    private fun drawPageContent(
        page: PdfDocument.Page,
        pageNumber: Int,
        measurements: ComposeViewUtils.ViewMeasurements,
        availableHeight: Int
    ) {
        val canvas = page.canvas

        // Draw header
        measurements.headerBitmap?.let { bitmap ->
            canvas.drawBitmap(bitmap, 0f, 0f, null)
        }

        // Draw footer
        measurements.footerBitmap?.let { bitmap ->
            val footerY = page.info.pageHeight - (measurements.footerHeight ?: 0).toFloat()
            canvas.drawBitmap(bitmap, 0f, footerY, null)
        }

        // Draw body content for current page
        measurements.bodyBitmap?.let { bitmap ->
            val bodyStartY = (pageNumber - 1) * availableHeight
            val bodyHeight = minOf(
                availableHeight,
                measurements.bodyHeight - bodyStartY
            )

            val srcRect = android.graphics.Rect(
                0, bodyStartY,
                page.info.pageWidth,
                bodyStartY + bodyHeight
            )

            val destRect = android.graphics.Rect(
                0,
                measurements.headerHeight ?: 0,
                page.info.pageWidth,
                (measurements.headerHeight ?: 0) + bodyHeight
            )

            canvas.drawBitmap(bitmap, srcRect, destRect, null)
        }
    }

    private fun getPageDimensions(pageConfig: PageConfig): Pair<Int, Int> {
        val (width, height) = pageConfig.pageSize
            .toPixels(context.resources.displayMetrics.densityDpi)

        return when (pageConfig.pageOrientation) {
            PageOrientation.PORTRAIT -> width to height
            PageOrientation.LANDSCAPE -> height to width
        }
    }

    private fun saveDocument(document: PdfDocument, config: DocumentConfig): File {
        val file = File(context.cacheDir, "${config.name}.pdf")
        FileOutputStream(file).use { outputStream ->
            document.writeTo(outputStream)
        }
        cleanup(document, config)
        return file
    }

    private fun cleanup(document: PdfDocument, config: DocumentConfig) {
        document.close()
        config.headerView?.let { viewUtils.cleanupView(it) }
        viewUtils.cleanupView(config.bodyView)
        config.footerView?.let { viewUtils.cleanupView(it) }
    }
}