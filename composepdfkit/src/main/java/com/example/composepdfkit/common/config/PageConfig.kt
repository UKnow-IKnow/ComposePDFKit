package com.example.composepdfkit.common.config

import androidx.annotation.Keep
import androidx.compose.runtime.Stable

@Keep
@Stable
data class PageConfig(
    val pageSize: PageSize = PageSize.A4,
    val pageOrientation: PageOrientation = PageOrientation.PORTRAIT,
    val margins: Margins = Margins()
) {
    @Keep
    @Stable
    data class Margins(
        val top: Int = 0,
        val bottom: Int = 0,
        val left: Int = 0,
        val right: Int = 0
    )
}