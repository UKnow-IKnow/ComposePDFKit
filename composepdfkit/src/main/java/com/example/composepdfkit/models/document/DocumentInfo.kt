package com.example.composepdfkit.models.document

import com.example.composepdfkit.models.config.PageOrientation
import com.example.composepdfkit.models.config.PageSize

data class DocumentInfo(
    val pageCount: Int,
    val pageSize: PageSize,
    val orientation: PageOrientation,
    val metadata: DocumentMetadata,
    val encrypted: Boolean = false,
    val passwordProtected: Boolean = false
) {
    val isPortrait: Boolean
        get() = orientation.isPortrait()

    val isLandscape: Boolean
        get() = orientation.isLandscape()
}