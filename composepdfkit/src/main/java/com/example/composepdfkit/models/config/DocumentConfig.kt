package com.example.composepdfkit.models.config

import com.example.composepdfkit.models.document.DocumentMetadata

data class DocumentConfig(
    val pageSize: PageSize = PageSize.default,
    val orientation: PageOrientation = PageOrientation.default,
    val margins: DocumentMargins = DocumentMargins.Default,
    val compressionQuality: CompressionQuality = CompressionQuality.default,
    val metadata: DocumentMetadata = DocumentMetadata()
) {
    companion object {
        val Default = DocumentConfig()
    }
}